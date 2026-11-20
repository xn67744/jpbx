package com.av.jpbx.service.impl;

import com.av.jpbx.enums.FSEventType;
import com.av.jpbx.service.FsEslClient;
import com.av.jpbx.service.IEslEventService;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * ESL 事件处理：仅做监控/状态同步，拨号计划由 xml_curl dialplan 负责。
 */
@Slf4j
@Service
public class EslEventServiceImpl implements IEslEventService {

    @Autowired
    private FsEslClient fsEslClient;

    @Override
    public void parseEventContent(EslEvent eslEvent) {
        FSEventType fsEventType = FSEventType.getFSEventTypeByName(eslEvent.getEventName());
        if (fsEventType == null) {
            return;
        }
        Map<String, String> headersInfo = eslEvent.getEventHeaders();
        switch (fsEventType) {
            case CHANNEL_CREATE:
                log.debug("CHANNEL_CREATE {} -> {}",
                        headersInfo.get("Caller-Caller-ID-Number"),
                        headersInfo.get("Caller-Destination-Number"));
                break;
            case CHANNEL_HANGUP:
            case CHANNEL_HANGUP_COMPLETE:
                log.debug("CHANNEL_HANGUP uuid={} cause={}",
                        headersInfo.get("Unique-ID"), headersInfo.get("Hangup-Cause"));
                break;
            case CHANNEL_BRIDGE:
            case CHANNEL_UNBRIDGE:
                break;
            case CHANNEL_EXECUTE_COMPLETE:
                break;
            case DETECTED_SPEECH:
                log.debug("DETECTED_SPEECH: {}", headersInfo.get("Speech-Type"));
                break;
            case CUSTOM:
                handleCustom(headersInfo);
                break;
            default:
                break;
        }
    }

    private void handleCustom(Map<String, String> headersInfo) {
        String eClass = headersInfo.get("Event-Subclass");
        if (eClass == null) {
            return;
        }
        switch (eClass) {
            case "sofia::register":
                log.info("SIP register: {}@{}", headersInfo.get("from-user"), headersInfo.get("from-host"));
                break;
            case "sofia::unregister":
            case "sofia::expire":
                log.info("SIP {}: {}@{}", eClass, headersInfo.get("from-user"), headersInfo.get("from-host"));
                break;
            case "conference::maintenance":
                log.debug("conference action={}", headersInfo.get("Action"));
                break;
            case "callcenter::info":
                log.debug("callcenter CC-Action={}", headersInfo.get("CC-Action"));
                break;
            default:
                break;
        }
    }

    @Override
    public String getFreeswitchVar(String name) {
        try {
            return Optional.ofNullable(fsEslClient.sendApi("global_getvar", name))
                    .map(s -> s.split("\n")[0])
                    .orElse("");
        } catch (Exception e) {
            log.warn("getFreeswitchVar {} failed: {}", name, e.getMessage());
            return "";
        }
    }
}
