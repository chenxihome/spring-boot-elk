package com.cx.logclient.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Slf4j
public class JsonUtil {

    public static final ObjectMapper OBJECTMAPPER;

    static {
        OBJECTMAPPER = new ObjectMapper();
        OBJECTMAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T convertToObject(String jsonStr, Class<T> valueType) {
        try {
            if (StringUtil.isEmpty(jsonStr)) {
                return null;
            }
            return OBJECTMAPPER.readValue(jsonStr, valueType);
        } catch (IOException e) {
            log.error("fastJson convert to object exception",e);
        }
        return null;
    }

    public static <T> T convertToObject(String jsonStr, TypeReference<T> valueTypeRef) {
        try {
            if (StringUtil.isEmpty(jsonStr)) {
                return null;
            }
            return OBJECTMAPPER.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            log.error("fastJson convert to object exception",e);
        }

        return null;
    }

    public static <T> T convertToObject(String jsonStr, JavaType javaType) {
        try {
            if (StringUtil.isEmpty(jsonStr)) {
                return null;
            }
            return OBJECTMAPPER.readValue(jsonStr, javaType);
        } catch (Exception e) {
            log.error("fastJson convert to object exception",e);
        }

        return null;
    }

    public static String convertToJson(Object object) {

        try {
            if (object == null) {
                return null;
            }
            return OBJECTMAPPER.writeValueAsString(object);
        } catch (Exception e) {
            log.error("fastJson convert to json exception",e);
        }
        return null;
    }

    public static <T> T convertToObejctFromKey(String jsonStr, String key, Class<T> valueType) {
        try {
            JsonNode node = OBJECTMAPPER.readTree(jsonStr);
            String nodeString = node.get(key).textValue();
            return convertToObject(nodeString, valueType);
        } catch (Exception e) {
            log.error("fastJson convertToObejctFromKey exception",e);
        }
        return null;

    }

    public static <T> T convertToObject(Class<T> valueType, Map<String, Object> params) {
        try {
            if (CollectionUtils.isEmpty(params)) {
                return null;
            }
            return OBJECTMAPPER.readValue(OBJECTMAPPER.writeValueAsString(params), valueType);
        } catch (Exception e) {
            log.error("fastJson convert to object exception",e);
        }
        return null;
    }
}