package com.thirdparty.rabbitMQ.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * @author lagon
 * @time 2017/7/5 18:36
 * @description MQ交易类公共请求基础报文
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MQReqMessage<T extends MQRequestBody> extends MQMessage {

    //请求时间
    protected String requestTime= DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
    //请求体
    protected T requestBody;

}
