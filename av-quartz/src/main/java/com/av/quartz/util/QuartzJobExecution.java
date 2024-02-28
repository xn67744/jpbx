package com.av.quartz.util;

import com.av.quartz.domain.SysJob;
import org.quartz.JobExecutionContext;
import com.av.quartz.domain.SysJob;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author long
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
