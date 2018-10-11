package com.cx.logclient.es;

/**
 * Created by tukun on 2017/12/6.
 */
public enum MappingEnum {
    /**
     * product mapping
     */
    PRODUCT_MAPPING("application-log","getProductMapping")
    ;
    private String indexName;
    private String mappingMethodName;

    public String getIndexName() {
        return indexName;
    }

    public String getMappingMethodName() {
        return mappingMethodName;
    }

    private MappingEnum(String indexName, String mappingMethodName) {
        this.indexName = indexName;
        this.mappingMethodName = mappingMethodName;
    }

    public static String getMappingMethodName(String indexName) {
        for (MappingEnum   r : MappingEnum.values()) {
            if (r.getIndexName().equals(indexName)) {
                return r.getMappingMethodName();
            }
        }
        return "";
    }
}
