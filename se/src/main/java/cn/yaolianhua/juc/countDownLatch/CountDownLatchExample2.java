package cn.yaolianhua.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-08 12:06
 **/
public class CountDownLatchExample2 {

    private static final int N = 5;
    private static final CountDownLatch doneSignal = new CountDownLatch(N);

    public static void main(String[] args) throws InterruptedException {

        ExecutorService e = Executors.newCachedThreadPool();
        for (int i = 0; i < N; i++)
            e.execute(new WorkerRunnable(doneSignal,i));

        doneSignal.await();
        e.shutdown();
        System.out.println("all thread finish");



    }


}
