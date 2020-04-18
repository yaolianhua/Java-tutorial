package cn.yaolianhua.juc.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-08 15:44
 **/
public class Worker implements Runnable{

    private final int row;
    private final CyclicBarrier barrier;

    public Worker(int row, CyclicBarrier barrier) {
        this.row = row;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            doWork(row);
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void doWork(int thread) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " doWork start ...");
        sleep(((long) (1000 * (thread + 1))));
        System.out.println(Thread.currentThread().getName() + " doWork end ...");
    }
}
