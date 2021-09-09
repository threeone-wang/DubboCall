package com.yitulin.dubbocall.infrastructure.utils;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/25 22:58
 * Modified By:
 */
public class JacksonUtil {

    private static ObjectMapper objectMapper=new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static Map<String,Object> object2Map(Object obj) {
        try {
            String asString = objectMapper.writeValueAsString(obj);
            return objectMapper.readValue(asString, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }

    public static Map<String,Object> string2Map(String str) {
        try {
            return objectMapper.readValue(str, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }

    public static <T> T string2Object(String s,Class<T> cls) {
        try {
            return objectMapper.readValue(s, cls);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
