package com.yitulin.dubbocall.infrastructure.utils;

import java.io.File;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/7 5:12 下午
 * modified : ⚡️
 */
public class JsonFileUtil {

    private static final Log LOG= LogFactory.get();

    public static JSON readFile(String filePath){
        return readFile(new File(filePath));
    }

    public static JSON readFile(File file){
        return JSONUtil.readJSON(file, CharsetUtil.CHARSET_UTF_8);
    }

    public static boolean overrideWrite(String filePath,JSON json){
        return overrideWrite(new File(filePath),json);
    }

    public static boolean overrideWrite(File file,JSON json){
        LOG.info("覆盖写文件：[{},{}]",file.getPath(),json);
        FileWriter fileWriter = FileWriter.create(file, CharsetUtil.CHARSET_UTF_8);
        fileWriter.write(json.toStringPretty());
        return true;
    }

}
