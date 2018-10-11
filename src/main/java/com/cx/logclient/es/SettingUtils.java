package com.cx.logclient.es;

/**
 * Created by tukun on 2017/12/14.
 */
public class SettingUtils {
    private final static String index="log-";
    public static String getLogIndexSetting() {
        return "{\"index\" : "
                + "{\"number_of_shards\" : \"6\","
                + "\"number_of_replicas\" : \"1\"}}";
    }
    public static String getSetting(String indexName){
        String setting = null;
         if(indexName.startsWith(index)){
             setting=getLogIndexSetting();
         }
        return setting;
    }
}
