package cn.yaolianhua.juc.future;

import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-11 10:08
 **/
public class Worker implements Callable<String> {

    private final int sleep;

    public Worker(int sleep) {
        this.sleep = sleep;
    }

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " task begin ...");
        sleep(sleep);
        return "Result of " + Thread.currentThread().getName();
    }
}
