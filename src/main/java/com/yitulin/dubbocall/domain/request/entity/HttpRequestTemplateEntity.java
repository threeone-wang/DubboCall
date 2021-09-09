package com.yitulin.dubbocall.domain.request.entity;

import com.yitulin.dubbocall.infrastructure.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/24 13:41
 * Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpRequestTemplateEntity {

    private String name;

    private String url;

    private String headerJson;

    private String bodyJson;

    public static HttpRequestTemplateEntity defaultEntity(){
        return HttpRequestTemplateEntity.builder()
                .name(Constants.DEFAULT_TEMPLATE_NAME)
                .url(Constants.DEFAULT_TEMPLATE_HTTP_URL)
                .headerJson(Constants.DEFAULT_TEMPLATE_HTTP_HEADER_JSON)
                .bodyJson(Constants.DEFAULT_TEMPLATE_HTTP_BODY_JSON)
                .build();
    }

}
