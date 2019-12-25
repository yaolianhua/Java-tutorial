package cn.yaolianhua.lock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-24 14:49
 **/
@Component
public class RedisLock implements Lock {
    private static final String REDIS_LOCK = "redis_lock";
    private static final String STOCK = "redis_lock_stock";
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Override
    public void lock() {
        while (true){
            Boolean ifAbsent = redisTemplate.boundValueOps(REDIS_LOCK).setIfAbsent(STOCK);
            if (ifAbsent)
                return;
        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        redisTemplate.delete(REDIS_LOCK);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
