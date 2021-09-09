package com.yitulin.dubbocall.infrastructure.utils;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/8 10:24 上午
 * modified : 💧💨🔥
 */
public class JsonFileUtilTest {

    @Test
    public void readFile(){
        String jsonFilePath="/Users/yitu/dubbo-call-settings.json";
        JSON json = JsonFileUtil.readFile(jsonFilePath);
        System.out.println(json);
    }

    @Test
    public void overrideWrite(){
        String jsonFilePath="/Users/yitu/dubbo-call-settings.json";
        JSON json = JsonFileUtil.readFile(jsonFilePath);
        System.out.println(json);
        JSONArray jsonArray= (JSONArray) json;
        jsonArray.remove(0);
        JsonFileUtil.overrideWrite(jsonFilePath,json);
    }

    public static void main(String[] args) {
        String jsonFilePath="/Users/yitu/dubbo-call-settings.json";
        File file = new File(jsonFilePath);
        System.out.println(file.getName());
        System.out.println(file.getPath());
        System.out.println(file.getAbsolutePath());
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
