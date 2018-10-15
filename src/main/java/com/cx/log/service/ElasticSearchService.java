package com.cx.log.service;

import com.cx.log.common.vo.LogMessage;

/**
 * @author: cx
 * @Description:
 * @Date: Created in 16:38 2018/10/10
 */
public interface ElasticSearchService {
    boolean saveToEs(LogMessage logMessage) throws Exception;
}
