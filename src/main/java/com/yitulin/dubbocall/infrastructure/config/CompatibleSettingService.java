package com.yitulin.dubbocall.infrastructure.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestCallLogEntity;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestTemplateEntity;
import com.yitulin.dubbocall.domain.request.entity.VariableEntity;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/9 2:17 下午
 * modified : 💧💨🔥
 */
public class CompatibleSettingService {

    private Settings settings = Settings.getInstance();

    private SettingService settingService = SettingService.getInstance();

    public static CompatibleSettingService getInstance(){
        Supplier<CompatibleSettingService> supplier = Suppliers.memoize(new Supplier<CompatibleSettingService>() {
            @Override
            public CompatibleSettingService get() {
                CompatibleSettingService compatibleSettingService = new CompatibleSettingService();
                return compatibleSettingService;
            }
        });
        return supplier.get();
    }

    public String readDefaultTemplateName(){
        if (settingService.configFileExists()){
            return settingService.getSettingAggregate().getDefaultTemplateName();
        }
        return settings.getDefaultTemplateName();
    }

    public List<HttpRequestTemplateEntity> readTemplateList(){
        if (settingService.configFileExists()){
            return settingService.getSettingAggregate().getHttpRequestTemplateEntityList();
        }
        return Lists.newArrayList(settings.getHttpRequestTemplateEntityMap().values());
    }

    public List<VariableEntity> readVariableList(){
        if (settingService.configFileExists()){
            return settingService.getSettingAggregate().getVariableEntityList();
        }
        return Lists.newArrayList(settings.getVariableEntityMap().values());
    }

    public Map<String, HttpRequestCallLogEntity> readLogMap(){
        if (settingService.configFileExists()){
            return settingService.getRequestCallLogEntityMap();
        }
        return settings.getRequestCallLogEntityMap();
    }

    public void writeVariable(List<VariableEntity> variableEntityList){
        if (settingService.configFileExists()){
            settingService.getSettingAggregate().setVariableEntityList(variableEntityList);
            settingService.overrideConfigFile();
        }else {
            Map<String, VariableEntity> variableEntityMap=variableEntityList.stream().collect(Collectors.toMap(item->item.getKey(),item->item));
            settings.setVariableEntityMap(variableEntityMap);
        }
    }

    public void writeLog(HttpRequestCallLogEntity logEntity){
        if (settingService.configFileExists()){
            settingService.getRequestCallLogEntityMap().put(logEntity.getMethodSignature(),logEntity);
            settingService.overrideLogFile();
        }else {
            settings.addNewLog(logEntity);
        }
    }

}
