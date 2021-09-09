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
public enum ConfigTypeEnum {

    /**
     * 自定义变量
     */
    CUSTOM("自定义变量","custom_value"),

    /**
     * 系统变量
     */
    SYSTEM("系统变量","-");

    private String type;
    private String value;


}
