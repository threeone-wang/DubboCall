package com.yitulin.dubbocall.infrastructure.enums;

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
public enum SystemConfigKeyEnum {

    /**
     * 工程
     */
    PROJECT_KEY("project"),

    /**
     * 包
     */
    PACKAGE_KEY("package"),

    /**
     * 接口
     */
    INTERFACE_KEY("interface"),

    /**
     * 方法
     */
    METHOD_KEY("method"),

    /**
     * 参数
     */
    PARAMS_KEY("params"),
    ;

    private String key;

}
