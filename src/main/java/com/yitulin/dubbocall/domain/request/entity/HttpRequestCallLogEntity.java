package com.yitulin.dubbocall.domain.request.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/25 22:26
 * Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpRequestCallLogEntity {

    private String methodSignature;

    private String url;

    private String headerJson;

    private String bodyJson;

    private LocalDateTime requestTime;

}
