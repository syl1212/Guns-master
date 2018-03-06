package com.stylefeng.guns.Redisson;

import com.stylefeng.guns.GunsApplication;
import com.sun.org.apache.xalan.internal.utils.FeatureManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * 文件名称： LockTest
 * 文件描述：
 * <p>
 * 公司部门： 东方银谷 JDK
 * 创建时间： 2018年03月06 15:02:29
 *
 * @author syl sunyuanli@yingu.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = GunsApplication.class) // 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration
public class LockTest {

    @Resource(name = "myRedissonClient")
    private RedissonClient redissonClient;

    @Test
    public void redissonTest(){
        SaleService service = new SaleService(redissonClient);
        for (int i = 0; i < 1000; i++) {
            SaleThread threadA = new SaleThread(service);
            threadA.start();
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
