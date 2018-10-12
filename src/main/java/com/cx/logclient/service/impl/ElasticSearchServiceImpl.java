package com.cx.logclient.service.impl;

import com.cx.logclient.es.ElasticSearchUtils;
import com.cx.logclient.es.MappingUtils;
import com.cx.logclient.service.ElasticSearchService;
import com.cx.logclient.util.ObjectUtil;
import com.cx.logclient.vo.EsLogVo;
import com.cx.logclient.vo.LogMessage;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SystemPropertyUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author: tukun
 * @Description:
 * @Date: Created in 16:37 2018/10/10
 */
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService{
    private ModelMapper mapper;

    public ElasticSearchServiceImpl() {
        this.mapper = new ModelMapper();
        this.mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public boolean saveToEs(LogMessage logMessage) throws Exception {
            Map<String,Object> map=convertLogMessageJson(logMessage);
            if(CollectionUtils.isEmpty(map)) {
                return false;
            }

        String indexName="log-"+getNowDateHourStr();
        String typeName=logMessage.getAppId();
        //判断es index,是否存在
        try {
            boolean isExist=ElasticSearchUtils.isExistsType(indexName);
            if(!isExist){
                //创建index type
                ElasticSearchUtils.createIndex(indexName);
                ElasticSearchUtils.createMapping(indexName,typeName, MappingUtils.getLogMapping());
            }
        } catch (Exception e) {
         return false;
        }
        //按小时存储日志
        return ElasticSearchUtils.saveDocument(indexName,typeName,map);
    }

    private  Map<String,Object> convertLogMessageJson(LogMessage message){
           Map<String, Object> jsonMap = null;
            EsLogVo vo = convetLogObject(message);
            try {
                jsonMap = ObjectUtil.objectToMap(vo);
            } catch (IllegalAccessException e) {
                //log.error("object 转map 异常："+e);
            }
            return jsonMap;
    }

    private EsLogVo convetLogObject(LogMessage message){
        EsLogVo vo=mapper.map(message,EsLogVo.class);
        vo.setTimeStamp(formatDateStr(message.getTimeStamp()));
        vo.setInsertTime(getNowDateStr());
        vo.setLogoutMessage(vo.getTimeStamp()+" ["
        +message.getThreadName()+"] "+message.getLevel()+" "+message.getLogName()+"--"+message.getMessage());
        return vo;
    }

    private String getNowDateStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private String formatDateStr(Long time) {
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    private String getNowDateHourStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd");
        return sdf.format(date);
    }
}
