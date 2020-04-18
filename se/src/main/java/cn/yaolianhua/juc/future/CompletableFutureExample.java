package cn.yaolianhua.juc.future;

import org.junit.Test;

import java.time.Clock;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-12 09:45
 **/
public class CompletableFutureExample {

    @Test
    public void completableFutureCombining() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> thenCompose = CompletableFuture.supplyAsync(() -> 34).thenCompose(r -> {//thenCompose => flapMap()
            try {
                System.out.println("calculating ...");
                sleep(1000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return CompletableFuture.supplyAsync(() -> r * r);
        });
        assertEquals(34 * 34, (int) thenCompose.get());

        System.out.println(" ------ thenCombine ------ ");
        CompletableFuture<Integer> thenCombine = CompletableFuture.supplyAsync(() -> 100).thenCombine(CompletableFuture.supplyAsync(() -> 100), (r1, r2) -> {//BiFunction
            try {
                System.out.println("calculating ...");
                sleep(2000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return r1 * r2;
        });
        assertEquals(10000,(int)thenCombine.get());


        System.out.println(" ----- thenAcceptBoth -----");
        CompletableFuture.supplyAsync(()->100).thenAcceptBoth(CompletableFuture.supplyAsync(()->100),(r1,r2)->{//BiConsumer
            try {
                System.out.println("calculating ...");
                sleep(3000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            System.out.println("completableFuture calculate result " + r1 * r2);
        });

        System.out.println(" ----- allOf -----");
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            System.out.println(currentThread().getName());
            return 10;
        });
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            System.out.println(currentThread().getName());
            return 20;
        });
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            System.out.println(currentThread().getName());
            return 30;
        });
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);// Void
        allOf.get();
        assertTrue(future1.isDone());
        assertTrue(future2.isDone());
        assertTrue(future3.isDone());

        System.out.println(" ------ join -----");
        Integer reduce = Stream.of(future1, future2, future3).map(CompletableFuture::join).reduce((a, b) -> a * b).orElse(0);
        assertEquals(6000,(int)reduce);
    }

    @Test
    public void completableFutureAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync() " + currentThread().getName());
            return "Hello";
        }).thenApplyAsync(r -> {
            System.out.println("thenApplyAsync() " + currentThread().getName());
            return r + " completableFuture";
        });
        assertEquals("Hello completableFuture",future.get());

        System.out.println( " ------------- ");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync() " + currentThread().getName());
            return "Hello";
        }).thenApply(r -> {
            System.out.println("thenApply() " + currentThread().getName());//main thread
            return r + " completableFuture";
        });
        assertEquals("Hello completableFuture",future2.get());
    }

    @Test
    public void completeFutureExceptionHandle() throws ExecutionException, InterruptedException {
        final int n = 0;
        CompletableFuture<Integer> handle = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return 10 / n;
        }).handle((s, t) -> {
            System.out.println("handle chain t : " + t);//handle chain t : java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero
            if (s != null)
                return s;
            else
                return 10;
        });
        assertEquals(10,(int)handle.get());

        System.out.println(" ************ ");

        CompletableFuture<Integer> exception = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return 10 / n;
        });
        Integer result = exception.get();//java.util.concurrent.ExecutionException: java.lang.ArithmeticException: / by zero
        assertNull(result);

        System.out.println(" &&&&&&&&&&&& ");
        CompletableFuture completableFuture = new CompletableFuture();
        completableFuture.completeExceptionally(new RuntimeException(" exception error"));
        completableFuture.get();//java.util.concurrent.ExecutionException: java.lang.RuntimeException:  exception error

    }

    @Test
    public void completableFutureWithProcessingResult() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return "completableFuture";
        }).thenApply(result -> result.concat(" processing new results"));// thenApply => map()
        String result = future.get();
        assertEquals("completableFuture processing new results",result);
    }

    @Test
    public void completableFutureWithProcessingVoid() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return "completableFuture";
        }).thenAccept(result -> System.out.println(result + " processing results"));
        Void aVoid = future.get();
        assertNull(aVoid);
    }

    @Test
    public void completableFutureWithProcessingNone() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return "completableFuture";
        }).thenRun(() -> System.out.println("completableFuture finished "));
        Void aVoid = future.get();
        assertNull(aVoid);
    }

    @Test
    public void completableFutureWithEncapsulatedComputationLogic() throws ExecutionException, InterruptedException {

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            //Supplier
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return "completableFuture";
        });

        String result = completableFuture.get();
        assertEquals("completableFuture",result);
    }

    @Test
    public void completableFutureWithEncapsulatedComputationLogic2() throws ExecutionException, InterruptedException {

        CompletableFuture<Void> completableFuture = //Runnable
                CompletableFuture.runAsync(() -> System.out.println("completableFuture"));
        Void aVoid = completableFuture.get();
        assertNull(aVoid);
    }

    @Test
    public void calculateAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Executors.newSingleThreadExecutor().submit(()->{
            try {
                System.out.println("Async thread " + currentThread().getName());
                sleep(2000);
                completableFuture.complete("completableFuture");
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return null;
        });
        System.out.println("completableFuture get() block");
        String result = completableFuture.get();
        assertEquals("completableFuture",result);
    }

    @Test
    public void calculateNonBlock() throws ExecutionException, InterruptedException {
        Future<String> future = CompletableFuture.completedFuture("completableFuture");
        assertEquals("completableFuture",future.get());
    }

    @Test
    public void calculateAsyncWithCancel() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        final long start = Clock.systemDefaultZone().millis();
        Executors.newSingleThreadExecutor().submit(()->{
            try {
                System.out.println("Async thread " + currentThread().getName());
                sleep(2000);
                long current = Clock.systemDefaultZone().millis();
                if (current - start > 1000) {//cancel the time is over 1000 millis
                    //this value has no effect in this implementation because interrupts are not used to control processing
                    completableFuture.cancel(false);
                }
                completableFuture.complete("completableFuture");
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return null;
        });
        System.out.println("completableFuture get() block");
        String result = completableFuture.get();//CancellationException
        assertEquals("completableFuture",result);
    }
}
