package com.yitulin.dubbocall.infrastructure.config;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestCallLogEntity;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestTemplateEntity;
import com.yitulin.dubbocall.domain.request.entity.VariableEntity;
import com.yitulin.dubbocall.infrastructure.common.Constants;
import com.yitulin.dubbocall.infrastructure.enums.ConfigTypeEnum;
import com.yitulin.dubbocall.infrastructure.enums.SystemConfigKeyEnum;

import lombok.Data;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/19 5:10 下午
 * Modified By:
 */
@Data
@State(name = "DubboCallSetting",storages = @Storage("dubbo-call-setting.xml"),defaultStateAsResource = true)
public class Settings implements PersistentStateComponent<Settings> {

    private Map<String,VariableEntity> variableEntityMap;

    private String defaultTemplateName;
    private Map<String, HttpRequestTemplateEntity> httpRequestTemplateEntityMap;

    private Map<String, HttpRequestCallLogEntity> requestCallLogEntityMap;

    public static Settings getInstance(){
        return ServiceManager.getService(Settings.class);
    }

    public Settings() {
        initDefaultSettings();
    }

    public void addNewLog(HttpRequestCallLogEntity logEntity){
        requestCallLogEntityMap.put(logEntity.getMethodSignature(),logEntity);
        if (requestCallLogEntityMap.size()>Constants.MAX_REQUEST_LOG_COUNT){
            List<HttpRequestCallLogEntity> requestCallLogEntityList = collectAndSortRequestCallLog();
            for (int i = 0; i < Constants.MAX_REQUEST_LOG_COUNT / 3; i++) {
                HttpRequestCallLogEntity discardLogEntity = requestCallLogEntityList.get(i);
                requestCallLogEntityMap.remove(discardLogEntity.getMethodSignature());
            }
        }
    }

    @Override
    public @Nullable Settings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Settings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public void noStateLoaded() {
        PersistentStateComponent.super.noStateLoaded();
        initDefaultSettings();
    }

    private void initDefaultSettings(){
        // 初始化默认http请求模板配置
        this.defaultTemplateName = Constants.DEFAULT_TEMPLATE_NAME;

        // 初始化变量配置
        List<VariableEntity> variableEntityList=
                Lists.newArrayList(
                        VariableEntity.builder().type(ConfigTypeEnum.SYSTEM.getType()).key(SystemConfigKeyEnum.PROJECT_KEY.getKey()).value(ConfigTypeEnum.SYSTEM.getValue()).build(),
                        VariableEntity.builder().type(ConfigTypeEnum.SYSTEM.getType()).key(SystemConfigKeyEnum.PACKAGE_KEY.getKey()).value(ConfigTypeEnum.SYSTEM.getValue()).build(),
                        VariableEntity.builder().type(ConfigTypeEnum.SYSTEM.getType()).key(SystemConfigKeyEnum.INTERFACE_KEY.getKey()).value(ConfigTypeEnum.SYSTEM.getValue()).build(),
                        VariableEntity.builder().type(ConfigTypeEnum.SYSTEM.getType()).key(SystemConfigKeyEnum.METHOD_KEY.getKey()).value(ConfigTypeEnum.SYSTEM.getValue()).build(),
                        VariableEntity.builder().type(ConfigTypeEnum.SYSTEM.getType()).key(SystemConfigKeyEnum.PARAMS_KEY.getKey()).value(ConfigTypeEnum.SYSTEM.getValue()).build()
                );
        Map<String, VariableEntity> variableEntityMap = variableEntityList.stream().collect(Collectors.toMap(VariableEntity::getKey, item -> item));
        this.variableEntityMap=variableEntityMap;

        // 初始化http请求模板配置
        Map<String, HttpRequestTemplateEntity> httpRequestTemplateEntityMap= Maps.newHashMap();
        HttpRequestTemplateEntity httpRequestTemplateEntity = HttpRequestTemplateEntity.builder()
                .name(Constants.DEFAULT_TEMPLATE_NAME)
                .url(Constants.DEFAULT_TEMPLATE_HTTP_URL)
                .headerJson(Constants.DEFAULT_TEMPLATE_HTTP_HEADER_JSON)
                .bodyJson(Constants.DEFAULT_TEMPLATE_HTTP_BODY_JSON)
                .build();
        httpRequestTemplateEntityMap.put(Constants.DEFAULT_TEMPLATE_NAME,httpRequestTemplateEntity);
        this.httpRequestTemplateEntityMap=httpRequestTemplateEntityMap;

        this.requestCallLogEntityMap=Maps.newHashMap();
    }

    private List<HttpRequestCallLogEntity> collectAndSortRequestCallLog(){
        return this.requestCallLogEntityMap.entrySet().stream().map(entry -> entry.getValue()).sorted(Comparator.comparing(HttpRequestCallLogEntity::getRequestTime))
                .collect(Collectors.toList());
    }
}
