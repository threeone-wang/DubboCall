package com.yitulin.dubbocall.infrastructure.utils;

import java.util.Map;
import java.util.Objects;

import com.yitulin.dubbocall.domain.request.entity.HttpRequestCallEntity;

import cn.hutool.core.map.MapUtil;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/26 11:22 上午
 * modified : 💧💨🔥
 */
public class ConcatUtil {

    public static String concatCurl(HttpRequestCallEntity callEntity){
        if (Objects.isNull(callEntity)){
            return "";
        }
        StringBuilder sb=new StringBuilder();
        sb.append("curl --location --request POST '").append(callEntity.getUrl()).append("' ");
        Map<String,String> headerMap = JsonUtil.from(callEntity.getHeaderJson(), Map.class);
        if (MapUtil.isNotEmpty(headerMap)){
            headerMap.entrySet().stream().forEach(entry-> sb.append("--header '").append(entry.getKey()).append(": ").append(entry.getValue()).append("' "));
        }
        sb.append("--data-raw '").append(callEntity.getBodyJson()).append("'");
        return sb.toString();
    }

}
