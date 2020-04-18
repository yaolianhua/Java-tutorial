package cn.yaolianhua.juc.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-07 16:16
 **/
public class SemaphoreExample {

    private static final Pool pool = new Pool();
    private static final int THREADS = 15;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < THREADS; i++) {
            executor.submit(()->{
                try {
                    task(1);
                } catch (InterruptedException e) {

                }
            });
        }
        executor.shutdown();

    }

    private static void task(int acquire) throws InterruptedException {
        Object item = pool.getItem(acquire);
        System.out.println();
        System.out.printf(" --- %s get acquire,get item [%s],get available permit [%s]",Thread.currentThread().getName(),item,pool.getAvailablePermits());
        sleep(2000);
        pool.putItem(item,acquire);
        System.out.println();
        System.out.printf(" *** %s release acquire,release item [%s],get available permit [%s]",Thread.currentThread().getName(),item,pool.getAvailablePermits());
    }

}
