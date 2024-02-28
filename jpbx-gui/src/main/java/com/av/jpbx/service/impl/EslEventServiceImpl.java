package com.av.jpbx.service.impl;


import com.av.common.utils.DateUtils;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxChangeNumber;
import com.av.jpbx.domain.TPbxRoute;
import com.av.jpbx.enums.FSEventType;
import com.av.jpbx.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
public class EslEventServiceImpl implements IEslEventService {

    @Autowired
    private ITPbxRouteService routeService;
    @Autowired
    private ITPbxChangeNumberService changeNumberService;
    @Autowired
    private ITPbxSysconfService sysconfService;

    @Override
    public void parseEventContent(EslEvent eslEvent) {
        FSEventType fsEventType = FSEventType.getFSEventTypeByName(eslEvent.getEventName());
        if(null != fsEventType) {
            Map<String, String> headersInfo = eslEvent.getEventHeaders();
            switch (fsEventType) {
                case CHANNEL_HANGUP_COMPLETE:
                    log.info("进入挂断完成事件");
                    break;
                case CHANNEL_HANGUP:
                    log.info("进入挂断事件");
                    break;
                case CHANNEL_CREATE:
                    if(null == headersInfo.get("variable_custom_originate")) {
                        doChannelCreateEvent(headersInfo);
                    }
                    break;
                case CHANNEL_BRIDGE:
                    break;
                case CHANNEL_UNBRIDGE:
                    break;
                case CUSTOM:
                    String eClass = headersInfo.get("Event-Subclass");
                    switch(eClass){
                        case "sofia::register":break;//注册
                        case "sofia::unregister":break;//取消注册
                        case "sofia::expire": break;///会话超时
                        case "conference::maintenance"://会议
                            String action = headersInfo.get("Action");
                            switch(action){
                                case "conference-create":  break;
                                case "conference-destroy":  break;
                                case "add-member":  break;
                                case "del-member":  break;
                            }
                    }
                default:
                    break;
            }
        }
    }

    private void doChannelCreateEvent(Map<String, String> headersInfo) {
        /**
         * 拨号计划
         * 呼出
         */
        String destNum = headersInfo.get("Caller-Destination-Number");
        String callerNum = headersInfo.get("Caller-Caller-ID-Number");
        List<TPbxRoute> routes = routeService.selectTPbxRouteList(new TPbxRoute().setReg(destNum).setType(1L));
        //号码变换
        if(CollectionUtils.isNotEmpty(routes)){
            for(TPbxRoute route : routes){
                if(StringUtils.isBlank(route.getGatewayIds())){
                    continue;
                }
                List<TPbxChangeNumber> changeNumbers = changeNumberService.selectTPbxChangeNumberList(new TPbxChangeNumber().setRouteId(route.getId()));
                if(CollectionUtils.isNotEmpty(changeNumbers)){
                    for(TPbxChangeNumber changeNumber: changeNumbers){
                        if(changeNumber.getType() == 0 && callerNum.equals(changeNumber.getCaller())){
                            callerNum = changeNumber.getChangedCaller();
                        }else if(changeNumber.getType() == 1 && Pattern.matches(changeNumber.getCallerReg(),callerNum)){
                            callerNum = changeNumber.getChangedCaller();
                        }else if(changeNumber.getType() == 1 && Pattern.matches(changeNumber.getCalleeReg(),destNum)){
                            destNum = changeNumber.getChangedCallee();
                        }
                    }
                }
                //进位去位
                if(null != route.getCallerPreRemove() && route.getCallerPreRemove() > 1){
                    destNum = StringUtils.substring(callerNum,route.getCallerPreRemove().intValue(), callerNum.length());
                }
                if(StringUtils.isNotEmpty(route.getCallerPre())){
                    callerNum = route.getCallerPre()+callerNum;
                }
                if(null != route.getCalleePreRemove() && route.getCalleePreRemove() > 1){
                    destNum = StringUtils.substring(destNum,route.getCalleePreRemove().intValue(), destNum.length());
                }
                if(StringUtils.isNotEmpty(route.getCalleePre())){
                    destNum = route.getCalleePre()+destNum;
                }
                for(String gatewayId : route.getGatewayIds().split(",")){
                    String record = null != EslListenerServer.sysconf?EslListenerServer.sysconf.getAudioRecordPath()+"/"+ DateUtils.parseDateToStr("yyyy/MM/dd",new Date())+"/" +headersInfo.get("Call-UUID")+".wav":"";
                    String command = "{ignore_early_media=true,origination_caller_id_number="+ callerNum +",custom_originate=true, record_session="+record+"}sofia/gateway/"+gatewayId+"/"+ destNum + " &bridge(user/"+headersInfo.get("Caller-Caller-ID-Number")+")";
                    //String command = "{ignore_early_media=true,origination_caller_id_number=31002}sofia/external/sip:3001@117.144.83.69:65430";
                    log.info("执行ESL " + command);
                    EslListenerServer.client.sendSyncApiCommand("originate", command);
                }
            }

        }

    }


    @Override
    public String getFreeswitchVar(String name){
        return Optional.of(EslListenerServer.client.sendSyncApiCommand("global_getvar", name)).map(i -> i.getBodyLines().get(0)).orElse("");
    }

}

