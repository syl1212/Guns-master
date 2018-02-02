package com.thirdparty.rabbitmq.config;

import lombok.Getter;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/**
 * Created by syl on 2018/1/24.
 */
@Configuration
//@Conditional(DemoConsumerCondition.class)
public class RabbitDemoConfig {

    @Autowired
    @Qualifier("mqTaskExecutor")
    protected TaskExecutor mqTaskExecutor;

    @Value("${rabbitmq.demo.address}")
    private String serverAddress;
    @Value("${rabbitmq.demo.username}")
    private String userName;
    @Value("${rabbitmq.demo.password}")
    private String password;
    @Value("${rabbitmq.demo.vhost}")
    private String vhost;
    @Value("${rabbitmq.demo.trans.exchange.name}")
    protected String exchangeName;
    @Getter
    @Value("${rabbitmq.demo.trans.req.queue.name}")
    protected String reqQueueName;
    @Value("${rabbitmq.demo.trans.req.binding.pattern}")
    protected String reqBindingPattern;

    @Bean
    public ConnectionFactory demoRabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        System.out.print("serverAddress:"+serverAddress);
        connectionFactory.setAddresses(serverAddress);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vhost);
        connectionFactory.setPublisherConfirms(true);//设置发布确认
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin phoRabbitAdmin() {
        return new RabbitAdmin(demoRabbitConnectionFactory());
    }
}
