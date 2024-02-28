package com.av.jpbx.task;

import com.av.jpbx.service.IPbxLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("pbxTask")
@Slf4j
public class pbxTask {

    @Autowired
    private IPbxLogService logService;

    public void logTask(){
        try {
            logService.generateLogFile();
        } catch (IOException e) {
            log.error("日志同步任务失败！");
        }
    }

}
