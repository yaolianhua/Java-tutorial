package cn.yaolianhua.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-08 11:48
 **/
public class Worker implements Runnable {

    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    public Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        try {
            startSignal.await();
            doWork();
            doneSignal.countDown();
        } catch (InterruptedException e) {

        }
    }

    private void doWork() throws InterruptedException {
        System.out.println("current thread "+Thread.currentThread().getName()+" doWork start ...");
        sleep(5000);
        System.out.println("current thread "+Thread.currentThread().getName()+" doWork end ...");
    }
}
