package com.yitulin.dubbocall.infrastructure.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestCallLogEntity;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestTemplateEntity;
import com.yitulin.dubbocall.domain.request.entity.SettingAggregate;
import com.yitulin.dubbocall.domain.request.entity.VariableEntity;
import com.yitulin.dubbocall.infrastructure.common.Constants;
import com.yitulin.dubbocall.infrastructure.utils.JsonFileUtil;
import com.yitulin.dubbocall.infrastructure.utils.PropertiesComponentUtil;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/7 5:19 下午
 * modified : 💧💨🔥
 */
@Data
public class SettingService {

    private static final Log LOG = LogFactory.get();

    private String configFilePath="";
    private File configFile;
    private String logFilePath="";
    private File logFile;

    private SettingAggregate settingAggregate;

    private Map<String, HttpRequestCallLogEntity> requestCallLogEntityMap=Maps.newHashMap();

    public static SettingService getInstance(){
        Supplier<SettingService> supplier = Suppliers.memoize(new Supplier<SettingService>() {
            @Override
            public SettingService get() {
                SettingService settingService = new SettingService();
                String configFilePath = PropertiesComponentUtil.read(Constants.CONFIG_FILE_PATH_KEY);
                if (Strings.isNullOrEmpty(configFilePath)) {
                    settingService.settingAggregate =new SettingAggregate(true);
                    return settingService;
                }
                settingService.resetConfigFile(configFilePath);
                return settingService;
            }
        });
        return supplier.get();
    }

    public boolean configFileExists(){
        return Objects.nonNull(configFile) && configFile.exists();
    }

    public boolean logFileExists(){
        return Objects.nonNull(logFile) && logFile.exists();
    }

    public boolean containsTemplate(String templateName){
        List<String> collect = this.settingAggregate.getHttpRequestTemplateEntityList().stream().map(HttpRequestTemplateEntity::getName).collect(Collectors.toList());
        return collect.contains(templateName);
    }

    public void overrideConfigFile(){
        LOG.info("根据内存中的配置数据覆盖配置文件,this.settingAggregate:[{}]", settingAggregate);
        if (!this.configFileExists()){
            LOG.info("配置文件不存在，无法覆盖", settingAggregate);
            return;
        }
        JsonFileUtil.overrideWrite(configFile, JSONUtil.parse(settingAggregate));
    }

    public void overrideLogFile(){
        LOG.info("根据内存中的记录数据覆盖记录文件,this.requestCallLogEntityMap:[{}]", requestCallLogEntityMap);
        if (!this.logFileExists()){
            LOG.info("记录文件不存在，无法覆盖", requestCallLogEntityMap);
            return;
        }
        JsonFileUtil.overrideWrite(logFile, JSONUtil.parse(requestCallLogEntityMap));
    }

    public void resetConfigFile(String configFilePath){
        LOG.info("根据文件路径重置配置数据,params:[{}]",configFilePath);
        this.configFilePath=configFilePath;
        PropertiesComponentUtil.write(Constants.CONFIG_FILE_PATH_KEY,this.configFilePath);
        this.configFile = new File(configFilePath);
        this.logFilePath=this.configFile.getParentFile().getPath()+"/dubbo-call-log.json";
        this.logFile = new File(this.logFilePath);
        this.resetFromConfigFile();
    }

    public void resetConfigFile(File configFile){
        LOG.info("根据文件重置配置数据,params:[{}]",configFile.getPath());
        this.configFilePath=configFile.getPath();
        PropertiesComponentUtil.write(Constants.CONFIG_FILE_PATH_KEY,this.configFilePath);
        this.configFile = configFile;
        this.logFilePath=this.configFile.getParentFile().getPath()+"/dubbo-call-log.json";
        this.logFile = new File(this.logFilePath);
        this.resetFromConfigFile();
    }

    public void initLogFile(){
        if (this.logFile.exists()){
            return;
        }
        try {
            this.logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.overrideLogFile();

    }

    public void resetFromConfigFile(){
        JSON json = JsonFileUtil.readFile(this.configFile);
        LOG.info("重置配置数据,json:[{}]",json);
        JSONObject jsonObject= (JSONObject) json;
        String defaultTemplateName = ((JSONObject) json).getStr("defaultTemplateName");
        JSONArray variableEntityList = ((JSONObject) json).getJSONArray("variableEntityList");
        List<VariableEntity> variableEntities = variableEntityList.toList(VariableEntity.class);
        JSONArray httpRequestTemplateEntityList = ((JSONObject) json).getJSONArray("httpRequestTemplateEntityList");
        List<HttpRequestTemplateEntity> httpRequestTemplateEntities = httpRequestTemplateEntityList.toList(HttpRequestTemplateEntity.class);
        this.settingAggregate = SettingAggregate.builder()
                .defaultTemplateName(defaultTemplateName)
                .variableEntityList(variableEntities)
                .httpRequestTemplateEntityList(httpRequestTemplateEntities)
                .build();
        this.initLogFile();
        JSON logJson = JsonFileUtil.readFile(this.logFile);
        LOG.info("重置记录数据,logJson:[{}]",logJson);
        JSONObject logJsonObject= (JSONObject) logJson;
        logJsonObject.keySet().stream().forEach(key->{
            HttpRequestCallLogEntity httpRequestCallLogEntity = logJsonObject.get(key, HttpRequestCallLogEntity.class);
            requestCallLogEntityMap.put(key,httpRequestCallLogEntity);
        });
    }

}
