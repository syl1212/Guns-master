package com.stylefeng.guns.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 文件名称： RedissonClientConfig
 * 文件描述：
 * <p>
 * 公司部门： 东方银谷 JDK
 * 创建时间： 2018年03月06 14:59:45
 *
 * @author syl sunyuanli@yingu.com
 * @version 1.0
 */
@Configuration
public class RedissonClientConfig {

    @Bean(name = "myRedissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        RedissonClient redissonClient = Redisson.create(Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));
        return redissonClient;
    }
}
