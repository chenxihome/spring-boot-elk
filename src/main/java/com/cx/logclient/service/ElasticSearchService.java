package com.cx.logclient.service;

import com.cx.logclient.vo.LogMessage;

/**
 * @author: tukun
 * @Description:
 * @Date: Created in 16:38 2018/10/10
 */
public interface ElasticSearchService {
    boolean saveToEs(LogMessage logMessage) throws Exception;
}