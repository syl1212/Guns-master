package com.thirdparty.rabbitmq.consumer.processor.demo;

import com.thirdparty.rabbitmq.base.ResDetail;
import org.springframework.amqp.core.MessageProperties;

import java.util.Set;

/**
 * @author syl
 * @description 消息处理接口
 */
public interface DemoMessageProcessor<T> {

    //获取数据传输格式，支持json和xml格式
    String getDataTransferFormat();

    //获取泛型的具体类型
    Class<T> getTClass();

    //获取被处理的routingKey集合
    Set<String> getProcessedRoutingKeys();

    //处理消息，返回结果
    ResDetail process(MessageProperties messageProperties, T message);


}
