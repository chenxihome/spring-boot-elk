package com.cx.logclient.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: tukun
 * @Description:
 * @Date: Created in 16:56 2018/9/14
 */
@Setter
@Getter
public class LogMessage implements Serializable {
    private String appId;
    private long  timeStamp;
    private String logName;
    private String threadName;
    private String  message;
    private String level;
    private String hostIp;
}
