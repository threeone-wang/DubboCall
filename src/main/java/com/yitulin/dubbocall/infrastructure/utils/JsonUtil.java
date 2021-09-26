package com.yitulin.dubbocall.infrastructure.utils;

import com.alibaba.fastjson.JSONValidator;
import com.google.gson.Gson;

import cn.hutool.json.JSONUtil;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/26 3:40 下午
 * Modified By:
 */
public class JsonUtil {

    public static <T> T from(String jsonStr,Class<T> cls){
        Gson gson=new Gson();
        return gson.fromJson(jsonStr, cls);
    }

    public static String tightJsonStr(String jsonStr){
        if (!JSONValidator.from(jsonStr).validate()){
            return jsonStr;
        }else if (JSONUtil.isJsonObj(jsonStr)){
            return JSONUtil.toJsonStr(JSONUtil.parseObj(jsonStr));
        }else if (JSONUtil.isJsonArray(jsonStr)){
            return JSONUtil.toJsonStr(JSONUtil.parseArray(jsonStr));
        }
        return jsonStr;
    }

    public static String prettyJsonStr(String jsonStr){
        if (JSONValidator.from(jsonStr).validate()){
            return JSONUtil.toJsonPrettyStr(jsonStr);
        }
        return jsonStr;
    }

}
