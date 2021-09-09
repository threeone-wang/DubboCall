package com.yitulin.dubbocall.domain.request.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/25 22:28
 * Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MethodDetailEntity {

    private String methodSignature;

    @JsonProperty("project")
    private String projectName;

    @JsonProperty("package")
    private String packagePath;

    @JsonProperty("interface")
    private String interfaceName;

    @JsonProperty("method")
    private String methodName;

    @JsonProperty("params")
    private String paramsJsonStr;

}
