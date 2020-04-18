package cn.yaolianhua.juc.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-11 10:16
 **/
public class FutureExample {


    private static final Worker worker = new Worker(2000);
    //run it one by one
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

//            futureAndCallable();
            futureIsDone();
//            futureCancel();
//            futureGetTimeout();
//        invokeAll();
//        invokeAny();
    }


    private static void invokeAll() throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        List<Callable<String>> workers = new ArrayList<>();
        IntStream.range(0,3).forEach(i -> workers.add(new Worker((i + 1) * 1000)));
        List<Future<String>> futures = threadPool.invokeAll(workers);
        //Any call to future.get() will block until all the Futures are complete
        for (Future<String> future : futures)
            System.out.println(future.get());

        threadPool.shutdown();
    }

    private static void invokeAny() throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        List<Callable<String>> workers = new ArrayList<>();
        IntStream.range(0,3).forEach(i -> workers.add(new Worker((i + 1) * 1000)));
        // Returns the result of the fastest callable,it does not return a Future
        String result = threadPool.invokeAny(workers);

        System.out.println(result);

        threadPool.shutdown();
    }

    private static void futureAndCallable() throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(worker);
        System.out.println("Do something else while callable getting execute");
        System.out.println("getting result " + future.get());// Future.get() blocks until the result is available

        executor.shutdown();
    }

    private static void futureGetTimeout() throws ExecutionException {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(worker);
        System.out.println("Do something else while callable getting execute");
        try {
            System.out.println("getting result " + future.get(1, TimeUnit.SECONDS));// Future.get() blocks until the result is available
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }finally {

            executor.shutdown();
        }

    }

    private static void futureIsDone() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(worker);
        while (!future.isDone()){
            System.out.println("Task is still not done ...");
            sleep(500);
        }
        System.out.println("Task is done");

        System.out.println("getting result " + future.get());

        executor.shutdown();

    }

    private static void futureCancel() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(worker);
        AtomicInteger count = new AtomicInteger(0);
        while (!future.isDone()){
            System.out.println("Task is still not done ...");
            count.incrementAndGet();
            sleep(200);
            if (count.get() > 5)//future cancel if calculate is still not done over 1s
                future.cancel(true);
        }
        System.out.println("Task is done");
        try {
            System.out.println("getting result " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }finally {

            executor.shutdown();
        }
    }


}
