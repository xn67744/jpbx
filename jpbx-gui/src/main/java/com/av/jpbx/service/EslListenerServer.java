package com.av.jpbx.service;

import com.av.jpbx.configure.FSConfigure;
import com.av.jpbx.domain.TPbxSysconf;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.outbound.AbstractOutboundClientHandler;
import org.freeswitch.esl.client.outbound.AbstractOutboundPipelineFactory;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.outbound.example.SimpleHangupOutboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ESL 启动门面：委托 {@link FsEslClient}，保留静态字段兼容旧代码。
 */
@Slf4j
@Component
public class EslListenerServer {

    /** @deprecated 请使用 FsEslClient */
    public static Client client;

    public static SocketClient outboundServer;

    public static TPbxSysconf sysconf = null;

    @Autowired
    private FsEslClient fsEslClient;

    @Autowired
    private FSConfigure fsConfigure;

    public synchronized void start(String host, int port, String pwd, int outBoundPort) {
        fsEslClient.connect();
        if (fsConfigure.isEslOutboundEnabled() && outboundServer == null) {
            try {
                outboundServer = new SocketClient(outBoundPort, new AbstractOutboundPipelineFactory() {
                    @Override
                    protected AbstractOutboundClientHandler makeHandler() {
                        return new SimpleHangupOutboundHandler();
                    }
                });
                outboundServer.start();
                log.info("ESL outbound server started on {}", outBoundPort);
            } catch (Exception e) {
                log.error("start outbound ESL failed: {}", e.getMessage());
            }
        }
        sysconf = fsEslClient.getSysconf();
    }

    public synchronized void stop() {
        fsEslClient.disconnect();
        if (outboundServer != null) {
            try {
                outboundServer.stop();
            } catch (Exception e) {
                log.warn("stop outbound ESL: {}", e.getMessage());
            }
            outboundServer = null;
        }
    }
}
