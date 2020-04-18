package cn.yaolianhua.juc.future;

import static java.lang.Thread.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-11 10:08
 **/
public class Worker2 implements Runnable {

    private final int sleep;

    public Worker2(int sleep) {
        this.sleep = sleep;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Task begin ...");
        try {
            sleep(sleep);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + " Task end ...");
    }
}
