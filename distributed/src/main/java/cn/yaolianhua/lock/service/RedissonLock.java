package cn.yaolianhua.lock.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-24 14:49
 **/
@Component
public class RedissonLock implements Lock {
    private static final String REDISSON_LOCK = "redisson_lock";
    @Autowired
    RedissonClient redissonClient;


    @Override
    public void lock() {

        RLock lock = redissonClient.getLock(REDISSON_LOCK);
        lock.lock();


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
        RLock lock = redissonClient.getLock(REDISSON_LOCK);
        lock.unlock();
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
