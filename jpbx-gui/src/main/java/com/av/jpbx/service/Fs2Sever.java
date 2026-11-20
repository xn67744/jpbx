package com.av.jpbx.service;

import com.av.jpbx.configure.FSConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 启动并保持 ESL 连接。
 */
@Slf4j
@Service("freeswitch")
@Order(value = 5)
public class Fs2Sever implements CommandLineRunner {

    @Autowired
    private FSConfigure fsConfigure;

    @Autowired
    private EslListenerServer eslServer;

    @Autowired
    private FsEslClient fsEslClient;

    private final AtomicBoolean keepAliveStarted = new AtomicBoolean(false);

    @Async
    @Override
    public void run(String... arg0) {
        try {
            connectServer();
        } catch (Exception e) {
            log.error("启动 ESL 服务失败", e);
        } finally {
            keepAlive();
        }
    }

    public void connectServer() {
        eslServer.start(fsConfigure.getEslHost(), fsConfigure.getEslInBoundPort(),
                fsConfigure.getEslpwd(), fsConfigure.getEslOutBoundPort());
    }

    public void releaseServer() {
        eslServer.stop();
    }

    public void keepAlive() {
        if (!keepAliveStarted.compareAndSet(false, true)) {
            return;
        }
        ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "fs-esl-keepalive");
            t.setDaemon(true);
            return t;
        });
        threadPool.scheduleAtFixedRate(() -> {
            if (!fsEslClient.isConnected()) {
                log.warn("ESL 断开，正在重连...");
                try {
                    connectServer();
                } catch (Exception e) {
                    log.error("ESL 重连失败: {}", e.getMessage());
                }
            }
        }, 3, 60, TimeUnit.SECONDS);
    }
}
