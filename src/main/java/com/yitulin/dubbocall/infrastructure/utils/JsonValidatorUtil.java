package com.yitulin.dubbocall.infrastructure.utils;

import com.alibaba.fastjson.JSONValidator;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/26 11:20 上午
 * modified : 💧💨🔥
 */
public class JsonValidatorUtil {

    public static boolean valid(String jsonStr){
        JSONValidator validator=JSONValidator.from(jsonStr);
        return validator.validate();
    }

}
