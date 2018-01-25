package com.thirdparty.rabbitMQ.base;

import lombok.*;

/**
 * @author syl
 * @description 响应实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResDetail {
    private boolean isSuccess;//是否成功
    private String remark;//响应描述
}
