package com.yitulin.dubbocall.infrastructure.utils;

import com.intellij.ide.util.PropertiesComponent;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/7 8:44 下午
 * modified : 💧💨🔥
 */
public class PropertiesComponentUtil {

    private static final Log LOG = LogFactory.get();

    private static final PropertiesComponent PROPERTIES_COMPONENT = PropertiesComponent.getInstance();


    public static void write(String key,String value){
        LOG.info("IDEA插件数据持久化,params:[{},{}]",key,value);
        PROPERTIES_COMPONENT.setValue(key, value);
    }

    public static String read(String key){
        return PROPERTIES_COMPONENT.getValue(key);
    }

}
