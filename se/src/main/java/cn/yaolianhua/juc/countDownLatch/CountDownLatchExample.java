package cn.yaolianhua.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-08 12:06
 **/
public class CountDownLatchExample {

    private static final int N = 5;
    private static final CountDownLatch startSignal = new CountDownLatch(1);
    private static final CountDownLatch doneSignal = new CountDownLatch(N);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < N; i++)
            new Thread(new Worker(startSignal,doneSignal),"Thread-"+(i+1)).start();

        doSomethingElse();
        startSignal.countDown();
        doSomething();
        doneSignal.await();
        System.out.println("all thread finish");



    }

    private static void doSomethingElse() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() +" doSomethingElse start ...");
        sleep(2000);
        System.out.println(Thread.currentThread().getName() + " doSomethingElse end ...");
    }

    private static void doSomething() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " doSomething start ...");
        sleep(3000);
        System.out.println(Thread.currentThread().getName() + " doSomething end ...");
    }

}
