package com.cx.log.service.impl;

import com.cx.log.service.ElasticSearchService;
import com.cx.log.service.LogManageService;
import com.cx.log.common.util.JsonUtil;
import com.cx.log.common.vo.LogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: cx
 * @Description:
 * @Date: Created in 16:32 2018/10/10
 */
@Service
public class LogManageServiceImpl implements LogManageService {
    @Autowired
    private ElasticSearchService elasticSearchService;

    @Override
    public boolean onMessage(String message) {
        try {
            LogMessage messagevo = JsonUtil.convertToObject(message, LogMessage.class);
            return elasticSearchService.saveToEs(messagevo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
