package com.yitulin.dubbocall.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/24 13:51
 * Modified By:
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     * 全局配置设置错误
     */
    GLOBAL_CONFIG_ERROR("10000","全局配置设置错误"),

    /**
     * HTTP请求模板设置错误
     */
    HTTP_REQUEST_TEMPLATE_ERROR("10100","HTTP请求模板设置错误"),
    ;

    private String code;
    private String title;

}
