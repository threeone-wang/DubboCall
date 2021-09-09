package com.yitulin.dubbocall.infrastructure.enums;

import java.util.Map;

import com.google.common.base.Strings;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestTemplateEntity;
import com.yitulin.dubbocall.domain.request.entity.VariableEntity;
import com.yitulin.dubbocall.infrastructure.config.Settings;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/23 6:40 下午
 * Modified By:
 */
@AllArgsConstructor
@Getter
public enum ImportTypeEnum {

    /**
     * 变量
     */
    VARIABLE("variable"){
        @Override
        public void refreshSetting(JSONObject jsonObject, Settings settings) {
            VariableEntity variableEntity = VariableEntity.builder()
                    .type(ConfigTypeEnum.CUSTOM.getType())
                    .key(jsonObject.getStr("key"))
                    .value(jsonObject.getStr("value"))
                    .build();
            Map<String, VariableEntity> variableEntityMap = settings.getVariableEntityMap();
            variableEntityMap.put(variableEntity.getKey(),variableEntity);
            settings.setVariableEntityMap(variableEntityMap);
        }
    },

    /**
     * 模板
     */
    TEMPLATE("template") {
        @Override
        public void refreshSetting(JSONObject jsonObject, Settings settings) {
            HttpRequestTemplateEntity requestTemplateEntity = HttpRequestTemplateEntity.builder()
                    .name(jsonObject.getStr("name"))
                    .url(jsonObject.getStr("url"))
                    .headerJson(jsonObject.getStr("headerJson"))
                    .bodyJson(jsonObject.getStr("bodyJson"))
                    .build();
            Map<String, HttpRequestTemplateEntity> templateEntityMap = settings.getHttpRequestTemplateEntityMap();
            templateEntityMap.put(requestTemplateEntity.getName(),requestTemplateEntity);
            settings.setHttpRequestTemplateEntityMap(templateEntityMap);
        }
    },

    /**
     * 默认模板
     */
    DEFAULT_TEMPLATE("default_template") {
        @Override
        public void refreshSetting(JSONObject jsonObject, Settings settings) {
            settings.setDefaultTemplateName(jsonObject.getStr("name"));
        }
    },

    ;

    private String type;

    public abstract void refreshSetting(JSONObject jsonObject, Settings settings);

    public static ImportTypeEnum getByType(String type){
        if (Strings.isNullOrEmpty(type)){
            return null;
        }
        for (ImportTypeEnum typeEnum : ImportTypeEnum.values()) {
            if (typeEnum.getType().equals(type)){
                return typeEnum;
            }
        }
        return null;
    }

}
