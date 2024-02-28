package com.av.jpbx.service;

import com.av.jpbx.domain.TPbxSysconf;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.outbound.AbstractOutboundClientHandler;
import org.freeswitch.esl.client.outbound.AbstractOutboundPipelineFactory;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.outbound.example.SimpleHangupOutboundHandler;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class EslListenerServer {

    public static Client client;

    public static SocketClient outboundServer;

    public static TPbxSysconf sysconf = null;

    @Autowired
    private IEslEventService eslEventService;

    @Autowired
    private ITPbxSysconfService sysconfService;

    public synchronized void start(String host, int port, String pwd, int outBoundPort) {
        try {
            if(null == client) {
                client = new Client();
            }
            client.connect(host, port, pwd, 15000);
            client.addEventListener(new IEslEventListener() {
                @Override
                public void eventReceived(EslEvent eslEvent) {
                    eslEventService.parseEventContent(eslEvent);
                }

                @Override
                public void backgroundJobResultReceived(EslEvent eslEvent) {
                    log.debug("事件名称++++" + eslEvent.getEventName());
                }
            });
            //开始监听
            client.setEventSubscriptions("plain", "all");

            if(null == outboundServer){
                outboundServer = new SocketClient(
                    outBoundPort,
                    new AbstractOutboundPipelineFactory() {
                        @Override
                        protected AbstractOutboundClientHandler makeHandler() {
                        //这个类可以自己重写一个
                        return new SimpleHangupOutboundHandler();
                        }
                    }
                );
                outboundServer.start();
            }

            sysconf = sysconfService.selectTPbxSysconfInfo();
        } catch (InboundConnectionFailure inboundConnectionFailure) {
            log.error("connect freeswitch error, " + host + ":" +port);
            inboundConnectionFailure.printStackTrace();
            client.close();
        }
    }


    public synchronized void stop() {
        if(null != client){
            client.close();
            outboundServer.stop();
        }
    }

}
