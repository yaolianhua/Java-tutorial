package cn.yaolianhua.juc.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-11 12:17
 **/
public class FutureTaskExample {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        FutureTask<String> futureTask1 = new FutureTask<>(new Worker2(2000)," futureTask1 is complete ");
        FutureTask<String> futureTask2 = new FutureTask<>(new Worker2(4000),"futureTask2 is complete ");

        threadPool.submit(futureTask1);
        threadPool.submit(futureTask2);

        while (true){
            try {
                if (futureTask1.isDone() && futureTask2.isDone()){
                    System.out.println("Both FutureTask completed");
                    threadPool.shutdown();
                    return;
                }

                if (!futureTask1.isDone())
                    System.out.println("FutureTask1 output = " + futureTask1.get());//it will block

                System.out.println("waiting for futureTask2 to complete ");
                String result = futureTask2.get(500, TimeUnit.MILLISECONDS);
                if (null != result)
                    System.out.println("FutureTask2 output = " + result);
            } catch (Exception e) {
//                System.out.println("Exception : " + e);
            }

        }

    }

}
