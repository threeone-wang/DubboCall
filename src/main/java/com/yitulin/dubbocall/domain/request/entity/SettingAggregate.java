package com.yitulin.dubbocall.domain.request.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.yitulin.dubbocall.infrastructure.common.Constants;
import com.yitulin.dubbocall.infrastructure.enums.ConfigTypeEnum;
import com.yitulin.dubbocall.infrastructure.enums.SystemConfigKeyEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/7 5:19 下午
 * modified : 💧💨🔥
 */
@Data
@Builder
@AllArgsConstructor
public class SettingAggregate {

    private List<VariableEntity> variableEntityList;

    private String defaultTemplateName;
    private List<HttpRequestTemplateEntity> httpRequestTemplateEntityList;

    public SettingAggregate() {
        this(false);
    }

    public SettingAggregate(boolean initFlag) {
        if (initFlag){
            initDefaultSettings();
        }
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
        this.variableEntityList=variableEntityList;

        // 初始化http请求模板配置
        HttpRequestTemplateEntity httpRequestTemplateEntity = HttpRequestTemplateEntity.builder()
                .name(Constants.DEFAULT_TEMPLATE_NAME)
                .url(Constants.DEFAULT_TEMPLATE_HTTP_URL)
                .headerJson(Constants.DEFAULT_TEMPLATE_HTTP_HEADER_JSON)
                .bodyJson(Constants.DEFAULT_TEMPLATE_HTTP_BODY_JSON)
                .build();
        this.httpRequestTemplateEntityList=Lists.newArrayList(httpRequestTemplateEntity);

    }


}
