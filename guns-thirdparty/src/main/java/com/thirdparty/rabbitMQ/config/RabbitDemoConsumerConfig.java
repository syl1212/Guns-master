package com.thirdparty.rabbitMQ.config;

import com.thirdparty.rabbitMQ.conditional.DemoConsumerCondition;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author lagon
 * @time 2017/10/18 15:48
 * @description 普惠运营MQ消费者配置类
 */
@Configuration
@Conditional(DemoConsumerCondition.class)
public class RabbitDemoConsumerConfig extends RabbitDemoConfig {


    /**
     交换器-4种模式:
     direct-exchange : 任何发送到Direct Exchange的消息都会被转发到RouteKey中指定的Queue
     fanout-exchange : 任何发送到Fanout Exchange的消息都会被转发到与该Exchange绑定(Binding)的所有Queue上
     topic-exchange	: 任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上
     headers-exchange: 忽略routingKey的一种路由方式,使用Headers来匹配的。Headers是一个键值对
     */
    /**
     queue 队列声明 - 消息队列
     durable:是否持久化
     exclusive: 仅创建者可以使用的私有队列，断开后自动删除，默认为false
     auto_delete: 当所有消费客户端连接断开后，是否自动删除队列，默认为false
     */
    //声明成功交换器
    @Bean
    @Lazy
    public TopicExchange phoResSuccessTopicExchange() {
        TopicExchange topicExchange = new TopicExchange(exchangeName);
        Queue queue = new Queue(reqQueueName, true);
        Binding queueBinding = BindingBuilder.bind(queue).to(topicExchange).with(reqBindingPattern);

        //声明交换器、队列、绑定规则
        RabbitAdmin phoRabbitAdmin= phoRabbitAdmin();
        phoRabbitAdmin.declareExchange(topicExchange);
        phoRabbitAdmin.declareQueue(queue);
        phoRabbitAdmin.declareBinding(queueBinding);
        return topicExchange;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory phoRabbitListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(demoRabbitConnectionFactory());
        factory.setTaskExecutor(mqTaskExecutor);
        factory.setPrefetchCount(1);//设置公平分发模式
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        return factory;
    }

}
