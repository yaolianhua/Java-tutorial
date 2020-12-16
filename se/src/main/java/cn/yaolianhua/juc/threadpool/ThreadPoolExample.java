package cn.yaolianhua.juc.threadpool;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-12-15 14:45
 **/
public class ThreadPoolExample {

    private final AtomicInteger c = new AtomicInteger(1);
    @Test
    public void threadPool() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1,true);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1,
                2,
                0,
                TimeUnit.MILLISECONDS,
                workQueue,
                runnable -> new Thread(runnable,"ThreadPoolExample-" + c.getAndIncrement()),
                (runnable,executor) -> System.out.println("Does nothing, discarding task runnable task: " + runnable));

        for (int i = 1; i < 6; i++) {
            pool.execute(producer(String.valueOf(i)));
        }
    }

    Runnable producer(String id){
        return new RunnableTask(id);
    }

}
