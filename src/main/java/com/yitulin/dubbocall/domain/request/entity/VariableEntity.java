package com.yitulin.dubbocall.domain.request.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/22 20:12
 * Modified By:
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariableEntity {

    private String type;

    private String key;

    private String value;

}
