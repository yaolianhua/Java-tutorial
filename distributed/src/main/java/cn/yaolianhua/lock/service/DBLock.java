package cn.yaolianhua.lock.service;

import cn.yaolianhua.bean.DbLockEntity;
import cn.yaolianhua.lock.repository.DBLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-14 15:54
 **/
@Component
@Transactional
public class DBLock implements Lock {
    private static final String DB_LOCK = "db_lock";

    @Autowired
    private DBLockRepository lockRepository;

    @Override
    public void lock() {
        while (true){

            DbLockEntity lockEntity = new DbLockEntity();
            lockEntity.setDbLock(DB_LOCK);
            DbLockEntity entity = lockRepository.save(lockEntity);
            if (null != entity)
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
        lockRepository.deleteByDbLock(DB_LOCK);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
