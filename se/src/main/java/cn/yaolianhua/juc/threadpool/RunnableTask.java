package cn.yaolianhua.juc.threadpool;

import java.util.concurrent.TimeUnit;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-12-15 15:01
 **/
public class RunnableTask implements Runnable{
    private final String id;

    public RunnableTask(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Runnable task-" + id + " executed in " + Thread.currentThread().getName());
    }

    @Override
    public String toString() {
        return "Task-" + id;
    }
}
