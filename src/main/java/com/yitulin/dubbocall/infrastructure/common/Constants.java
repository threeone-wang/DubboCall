package com.yitulin.dubbocall.infrastructure.common;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/19 7:56 下午
 * Modified By:
 */
public class Constants {

    private Constants() {
    }

    public static final String CONFIG_FILE_PATH_KEY="dubbocall.config.file.path";

    public static final String DEFAULT_TEMPLATE_NAME="DEFAULT";
    public static final String DEFAULT_TEMPLATE_HTTP_URL ="http://www.dubbocall.com";
    public static final String DEFAULT_TEMPLATE_HTTP_HEADER_JSON="{\"Content-Type\":\"application/json\"}";
    public static final String DEFAULT_TEMPLATE_HTTP_BODY_JSON ="${params}";

    public static final String TITLE_INFO = "DubboCall Title Info";

    public static final Integer MAX_REQUEST_LOG_COUNT = 100;

}
