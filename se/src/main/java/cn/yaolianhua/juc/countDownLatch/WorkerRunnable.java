package cn.yaolianhua.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-08 14:20
 **/
public class WorkerRunnable implements Runnable{

    private final CountDownLatch doneSignal;
    private final int i;

    public WorkerRunnable(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }

    @Override
    public void run() {
        try {
            doWork(i);
            doneSignal.countDown();
        } catch (InterruptedException e) {

        }
    }

    private void doWork(final int i) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " doWork start ...");
        sleep(((long) (1000 * (i+1))));
        System.out.println(Thread.currentThread().getName() + " doWork end ...");
    }

}
