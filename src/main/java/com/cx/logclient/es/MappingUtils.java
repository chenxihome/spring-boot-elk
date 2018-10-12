package com.cx.logclient.es;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by tukun on 2017/12/6.
 */
@Slf4j
public class MappingUtils {
    public static XContentBuilder getLogMapping(){
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("appId").field("type","text").endObject()
                    .startObject("logName").field("type","text").field("index","not_analyzed").endObject()
                    .startObject("threadName").field("type","text").field("index","not_analyzed").endObject()
                    .startObject("message").field("type","text").field("index","not_analyzed").endObject()
                    .startObject("level").field("type","text").field("index","not_analyzed").endObject()
                    .startObject("hostIp").field("type","text").field("index","not_analyzed").endObject()
                    .startObject("timeStamp").field("type","date").field("format","yyyy-MM-dd HH:mm:ss").endObject()
                    .startObject("insertTime").field("type","date").field("format","yyyy-MM-dd HH:mm:ss").endObject()
                    .startObject("logoutMessage").field("type","text").endObject()
                    .endObject()
                    .endObject();
        } catch (IOException e) {
          log.error("get product mapping exception",e);
        }
        return mapping;
    }
}
