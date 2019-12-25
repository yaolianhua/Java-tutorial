package cn.yaolianhua.lock.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static java.lang.Thread.currentThread;
import static jodd.util.ThreadUtil.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-24 17:35
 **/
@Component
@Scope(value = "prototype")
public class ZkLock implements Lock, InitializingBean, DisposableBean {
    @Autowired
    CuratorFramework curatorFramework;
    private InterProcessMutex mutex;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("start CuratorFramework client");
        curatorFramework.start();
        mutex = new InterProcessMutex(curatorFramework, "/curator/lock");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("close CuratorFramework client");
        curatorFramework.close();
    }

    /**
     * Curator 2.x.x - compatible with both ZooKeeper 3.4.x and ZooKeeper 3.5.x
     *
     * Curator 3.x.x - compatible only with ZooKeeper 3.5.x and includes support for new features such as dynamic reconfiguration, etc.
     */
    @Override
    public void lock() {

        System.out.println(currentThread().getName() + " acquire " );
        try {

            mutex.acquire();
            System.out.println(currentThread().getName() + " acquire success" );
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            System.out.println(Thread.currentThread().getName() + " release");
            mutex.release();
            System.out.println(currentThread().getName() + " release success ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
