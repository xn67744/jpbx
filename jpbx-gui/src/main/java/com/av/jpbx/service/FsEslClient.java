package com.av.jpbx.service;

import com.av.jpbx.configure.FSConfigure;
import com.av.jpbx.domain.TPbxSysconf;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FreeSWITCH ESL 入站客户端（可重连、线程安全命令发送）。
 */
@Slf4j
@Component
public class FsEslClient {

    @Autowired
    private FSConfigure fsConfigure;

    @Autowired
    private IEslEventService eslEventService;

    @Autowired
    @Lazy
    private ITPbxSysconfService sysconfService;

    private volatile Client client;
    private final AtomicBoolean listenerBound = new AtomicBoolean(false);
    private volatile TPbxSysconf sysconf;

    public synchronized void connect() {
        String host = fsConfigure.getEslHost();
        int port = fsConfigure.getEslInBoundPort();
        String pwd = fsConfigure.getEslpwd();
        try {
            if (client != null) {
                try {
                    if (client.canSend()) {
                        return;
                    }
                    client.close();
                } catch (Exception ignore) {
                    // ignore close errors
                }
                client = null;
                listenerBound.set(false);
            }
            Client c = new Client();
            c.connect(host, port, pwd, Math.max(fsConfigure.getConnTimeout(), 15) * 1000);
            if (listenerBound.compareAndSet(false, true)) {
                c.addEventListener(new IEslEventListener() {
                    @Override
                    public void eventReceived(EslEvent eslEvent) {
                        try {
                            eslEventService.parseEventContent(eslEvent);
                        } catch (Exception e) {
                            log.warn("handle ESL event error: {}", e.getMessage());
                        }
                    }

                    @Override
                    public void backgroundJobResultReceived(EslEvent eslEvent) {
                        log.debug("bg job: {}", eslEvent.getEventName());
                    }
                });
            }
            c.setEventSubscriptions("plain", "all");
            this.client = c;
            this.sysconf = sysconfService.selectTPbxSysconfInfo();
            // 兼容旧静态引用
            EslListenerServer.client = c;
            EslListenerServer.sysconf = this.sysconf;
            log.info("ESL connected {}:{}", host, port);
        } catch (InboundConnectionFailure e) {
            log.error("ESL connect failed {}:{} - {}", host, port, e.getMessage());
            this.client = null;
            EslListenerServer.client = null;
            listenerBound.set(false);
        }
    }

    public synchronized void disconnect() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                log.warn("ESL close error: {}", e.getMessage());
            }
            client = null;
            EslListenerServer.client = null;
            listenerBound.set(false);
        }
    }

    public boolean isConnected() {
        Client c = client;
        return c != null && c.canSend();
    }

    public TPbxSysconf getSysconf() {
        if (sysconf == null) {
            sysconf = sysconfService.selectTPbxSysconfInfo();
            EslListenerServer.sysconf = sysconf;
        }
        return sysconf;
    }

    public void refreshSysconf(TPbxSysconf conf) {
        this.sysconf = conf;
        EslListenerServer.sysconf = conf;
    }

    public String sendApi(String command, String args) {
        if (!isConnected()) {
            connect();
        }
        Client c = client;
        if (c == null || !c.canSend()) {
            throw new IllegalStateException("FreeSWITCH ESL 未连接");
        }
        EslMessage msg = c.sendSyncApiCommand(command, args == null ? "" : args);
        if (msg == null || msg.getBodyLines() == null) {
            return "";
        }
        return String.join("\n", msg.getBodyLines());
    }

    public List<String> sendApiLines(String command, String args) {
        if (!isConnected()) {
            connect();
        }
        Client c = client;
        if (c == null || !c.canSend()) {
            return Collections.emptyList();
        }
        EslMessage msg = c.sendSyncApiCommand(command, args == null ? "" : args);
        return msg == null || msg.getBodyLines() == null ? Collections.emptyList() : msg.getBodyLines();
    }

    @PreDestroy
    public void destroy() {
        disconnect();
    }
}
