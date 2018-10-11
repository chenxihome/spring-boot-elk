package com.cx.logclient.service.impl;

import com.cx.logclient.service.ElasticSearchService;
import com.cx.logclient.service.LogManageService;
import com.cx.logclient.util.JsonUtil;
import com.cx.logclient.vo.LogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: tukun
 * @Description:
 * @Date: Created in 16:32 2018/10/10
 */
@Service
public class LogManageServiceImpl implements LogManageService {
    @Autowired
    private ElasticSearchService elasticSearchService;
    @Override
    public boolean onMessage(String message) {
        try{
            LogMessage messagevo= JsonUtil.convertToObject(message, LogMessage.class);
            return elasticSearchService.saveToEs(messagevo);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
