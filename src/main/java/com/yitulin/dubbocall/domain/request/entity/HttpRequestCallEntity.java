package com.yitulin.dubbocall.domain.request.entity;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.yitulin.dubbocall.domain.request.entity.MethodDetailEntity;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/25 22:26
 * Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpRequestCallEntity {

    private MethodDetailEntity methodDetailEntity;

    private String url;
    private String headerJson;
    private String bodyJson;

    public String sendHttpPost(){
        HttpRequest httpRequest = HttpUtil.createPost(url);
        if (!JSONUtil.isJson(headerJson)){
            return "请求头json校验失败";
        }
        if (!JSONUtil.isJson(bodyJson)){
            return "请求体json校验失败";
        }
        httpRequest.timeout(3000);
        JSONObject jsonObject = JSONUtil.parseObj(headerJson);
        if (MapUtil.isNotEmpty(jsonObject)){
            Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
            entrySet.stream().forEach(item->httpRequest.header(item.getKey(), String.valueOf(item.getValue())));
        }
        httpRequest.body(bodyJson);
        HttpResponse httpResponse=null;
        try {
            httpResponse = httpRequest.executeAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Objects.isNull(httpResponse)){
            return "请求失败，返回信息为空";
        }
        return httpResponse.body();
    }

    public String concatLogKey(String templateName){
        return templateName+"_"+this.getMethodDetailEntity().getMethodSignature();
    }



}
