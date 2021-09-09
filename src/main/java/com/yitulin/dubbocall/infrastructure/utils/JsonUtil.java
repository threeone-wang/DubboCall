package com.yitulin.dubbocall.infrastructure.utils;

import com.alibaba.fastjson.JSONValidator;

import cn.hutool.json.JSONUtil;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/26 3:40 下午
 * Modified By:
 */
public class JsonUtil {

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
