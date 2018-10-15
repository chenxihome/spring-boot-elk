package com.cx.logclient.vo;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.cx.logclient.util.IpUtil;

/**
 * @author: cx
 * @Description:
 * @Date: Created in 17:02 2018/9/14
 */
public class LogMessageBuilder {
    private LogMessageBuilder(){

    }
    public static LogMessage build(ILoggingEvent event){
        LogMessage vo = new LogMessage();
        vo.setLogName(event.getLoggerName());
        vo.setThreadName(event.getThreadName());
        vo.setLevel(event.getLevel().levelStr);
        vo.setTimeStamp(event.getTimeStamp());
        vo.setHostIp(IpUtil.getLocalIP());
        return vo;
    }
}
