package com.cx.log.common.vo;

import lombok.Data;

/**
 * @author: cx
 * @Description:
 * @Date: Created in 17:01 2018/10/10
 */
@Data
public class EsLogVo {
    private String appId;
    private String timeStamp;
    private String logName;
    private String threadName;
    private String message;
    private String level;
    private String hostIp;
    private String insertTime;
    private String logoutMessage;
}
