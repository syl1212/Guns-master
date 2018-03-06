package com.stylefeng.guns.Redisson;

import com.stylefeng.guns.distributeLockTest.DistributedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * 文件名称： SaleService
 * 文件描述：
 * <p>
 * 公司部门： 东方银谷 JDK
 * 创建时间： 2018年03月02 14:31:01
 *
 * @author syl sunyuanli@yingu.com
 * @version 1.0
 */
public class SaleService {

    private static Logger logger = LoggerFactory.getLogger(SaleService.class);

    private RedissonClient redissonClient;

    public SaleService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    int n = 10000;

    public void seckill() {
        RLock lock = redissonClient.getLock("TEST");
        try {
            lock.lock();
            logger.info("Request Thread - " + Thread.currentThread().getId() + " locked and begun...");
            System.out.println(--n);
            logger.info("Request Thread - " + Thread.currentThread().getId() + " ended successfully...");
        } catch (Exception ex) {
            logger.error("Error occurred");
        } finally {
            lock.unlock();
            logger.info("Request Thread - " + Thread.currentThread().getId() + " unlocked...");
        }
    }
}
