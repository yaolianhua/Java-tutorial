package cn.yaolianhua.lock;

import cn.yaolianhua.bean.DbLockEntity;
import cn.yaolianhua.bean.DbStockEntity;
import cn.yaolianhua.lock.config.AppConfig;
import cn.yaolianhua.lock.repository.DBLockRepository;
import cn.yaolianhua.lock.repository.DBStockRepository;
import cn.yaolianhua.lock.service.*;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-14 09:49
 **/
public class LockTest {


    private AnnotationConfigApplicationContext context;
    private DBLockRepository lockRepository;
    private DBStockRepository stockRepository;
    private StockService stockService;
    private DBLock lock;
    private RedisLock redisLock;
    private RedissonLock redissonLock;
    private ZkLock zkLock1;
    private ZkLock zkLock2;
    private ZkLock zkLock3;
    private static final String DB_STOCK = "iPhone XR";
    private static final Integer DB_STOCK_NUM = 10;

    @Before
    public void init(){
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        lockRepository = context.getBean(DBLockRepository.class);
        stockRepository = context.getBean(DBStockRepository.class);
        stockService = context.getBean(StockService.class);
        lock = context.getBean(DBLock.class);
        redisLock = context.getBean(RedisLock.class);
        redissonLock = context.getBean(RedissonLock.class);
        zkLock1 = context.getBean(ZkLock.class);
        zkLock2 = context.getBean(ZkLock.class);
        zkLock3 = context.getBean(ZkLock.class);
    }

    @Test
    public void DIInfo(){
        System.out.println(context.getBean(RedisTemplate.class).hashCode());
        System.out.println(context.getBean(DruidDataSource.class).hashCode());
        System.out.println(zkLock1.hashCode());
        System.out.println(zkLock2.hashCode());
        System.out.println(zkLock3.hashCode());

    }

    @Test
    public void DBTest(){

        lockRepository.deleteByDbLock("db_lock");

        DbLockEntity lockEntity = lockRepository.findByDbLock("db_lock").orElse(null);
        assertNull(lockEntity);


        stockRepository.deleteAll();
        DbStockEntity stock = new DbStockEntity();
        stock.setDbStock(DB_STOCK);
        stock.setDbStockNum(DB_STOCK_NUM);
        DbStockEntity stockEntity = stockRepository.save(stock);
        assertEquals((long)DB_STOCK_NUM, stockEntity.getDbStockNum());

        DbStockEntity dbStockEntity = stockRepository.findByDbStock(DB_STOCK).orElse(null);
        assert dbStockEntity != null;
        dbStockEntity.setDbStockNum(10);
        stockRepository.save(dbStockEntity);
    }
    @Test
    public void StockReduce() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        ExecutorService e = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++)
        {
            AtomicInteger c = new AtomicInteger(i+1);
            e.execute(() -> {
//                lock.lock();
//                redisLock.lock();
//                redissonLock.lock();
                if (c.get() == 1)
                    zkLock1.lock();
                else if (c.get() == 2)
                    zkLock2.lock();
                else
                    zkLock3.lock();

                try {
                    stockService.stockReduce(DB_STOCK);
                    System.out.println(Thread.currentThread().getName());
                } finally {

//                    lock.unlock();
//                    redisLock.unlock();
//                    redissonLock.unlock();
                    if (c.get() == 1)
                        zkLock1.unlock();
                    else if (c.get() == 2)
                        zkLock2.unlock();
                    else
                        zkLock3.unlock();

                    latch.countDown();
                }
            });
        }

        latch.await();
        e.shutdown();

    }


}
