package cn.yaolianhua.juc.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-08 16:18
 **/
public class CyclicBarrierExample {
    private static final int N = 5;
    private static final CyclicBarrier barrier = new CyclicBarrier(N,()-> System.out.println(Thread.currentThread().getName() + " is the last thread entering barrier, it can do something else"));

    public static void main(String[] args) {
        for (int i = 0; i < N; i++)
            new Thread(new Worker(i,barrier)).start();

    }
}
