package com.yitulin.dubbocall.infrastructure.analysis;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/7/8 8:11 下午
 * Modified By:
 */
public class RussianDollMap extends HashMap<String,Object> {

    public static RussianDollMap create(){
        return new RussianDollMap();
    }

    public static boolean isRussianDollKey(String key){
        return key.indexOf(".")!=-1;
    }

    public static String[] russianDollKeys(String key){
        String firstKey = key.substring(0, key.indexOf("."));
        String secondKey = key.substring(key.indexOf(".")+1);
        return new String[]{firstKey,secondKey};
    }

    public void set(String key,Object value){
        if (!isRussianDollKey(key)){
            this.put(key,value);
            return;
        }
        String[] russianDollKeys = russianDollKeys(key);
        RussianDollMap subRussianDollMap=create();
        subRussianDollMap.set(russianDollKeys[1],value);
        this.put(russianDollKeys[0], subRussianDollMap);
    }

    public Object getValue(String key){
        if (!isRussianDollKey(key)){
            return this.get(key);
        }
        String[] russianDollKeys = russianDollKeys(key);
        RussianDollMap russianDollMap = this.getValue(russianDollKeys[0], RussianDollMap.class);
        return russianDollMap.get(russianDollKeys[1]);
    }

    public <T> T getValue(String key,Class<T> cls){
        return (T) this.get(key);
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }

}
