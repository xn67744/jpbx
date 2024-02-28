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

/**
 * @author nan.xie
 * @create 2021/12/6 11:20
 * @desc
 **/
@Slf4j
@Service("freeswitch")
@Order(value = 5)
public class Fs2Sever implements CommandLineRunner {


    @Autowired
    private FSConfigure fsConfigure;


    @Autowired
    private EslListenerServer eslServer;


    @Async
    @Override
    public void run(String... arg0) {
        /**
         * 1、判断需要对接加载哪些服务
         * 2、启动客户端的端口连接（TCP,UDP,WS）
         */
        try {
            connectServer();
        }catch(Exception e){
            log.error("****************启动**************** ESL 服务失败！");
            e.printStackTrace();
        }finally {
            keepAlive();
        }

    }

    public void connectServer() {
        eslServer.start(fsConfigure.getEslHost(), fsConfigure.getEslInBoundPort(), fsConfigure.getEslpwd(), fsConfigure.getEslOutBoundPort());
    }

    public void releaseServer() {
        eslServer.stop();
    }

    public void keepAlive() {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
        threadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(null == EslListenerServer.client || !EslListenerServer.client.canSend()) {
                    log.warn("服务断开，正在重连");
                    try {
                        eslServer.start(fsConfigure.getEslHost(), fsConfigure.getEslInBoundPort(), fsConfigure.getEslpwd(), fsConfigure.getEslOutBoundPort());
                    }catch(Exception e){
                        e.printStackTrace();
                        log.error("连接失败");
                    }
                }
            }
        }, 3, 60, TimeUnit.SECONDS); // 3s 之后执行
    }
}