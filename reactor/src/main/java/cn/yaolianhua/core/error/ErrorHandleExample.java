package cn.yaolianhua.core.error;

import org.junit.Test;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-09 09:31
 **/
public class ErrorHandleExample {

    @Test
    public void onErrorReturn(){
        Flux.just(1,2,0)
                .map(i -> "100 / " + i + " = " + (100 / i) )
                .onErrorReturn("Divided by zero")
                .subscribe(System.out::println);
    }

    @Test
    public void onErrorReturn2(){
        Flux<Integer> flux = Flux.just(1, 2, 0)
                .map(this::error)
                //还可以选择Predicate对异常应用，以决定是否恢复
                .onErrorReturn(e -> e.getMessage().equals("by zero"), 0);
        flux.subscribe(v-> System.out.println("RECEIVED " + v));
    }
    private int error(int i){
        try {
            return 10 / i;
        } catch (Exception e) {
            throw new RuntimeException("by zero");
        }
    }

    /**
     * try {
     *     for (int i = -4; i < 2; i++) {
     *         double v1 = doDangerous(i);
     *         double v2 = doSecondTransform(v1);
     *         System.out.println("RECEIVED " + v2);
     *     }
     * } catch (Throwable t) {
     *     System.err.println("CAUGHT " + t);
     * }
     */

    @Test
    public void tryCatch(){
        Flux<Double> flux = Flux.range(-4, 5)
                .map(this::doDangerous)//执行了可能引发异常的转换
                .map(this::doSecondTransform);//如果一切顺利，则执行第二次转换
        flux.subscribe(
                value -> System.out.println("RECEIVED " + value),//每个成功转换的值都会打印出来
                error -> System.out.println("CAUGHT " + error)
        );


    }

    private double doDangerous(int i){
        return new BigDecimal(10 / Math.abs(i)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    private double doSecondTransform(double j){
        return BigDecimal.valueOf(j).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Test
    public void onErrorResume(){
        Flux<String> flux = Flux.just("unknownKey", "timeout", "customKey")
                .flatMap(k ->
                        callExternalService(k).onErrorResume(error -> {
                            if (error instanceof TimeoutException)
                                return getFromLocalCache(k);
                            if (error instanceof UnsupportedOperationException)
                                return getFromLocalCache(k);
                            return Flux.error(error);
                        })
                );
        flux.subscribe(v -> System.out.println("RECEIVED " + v));

    }

    @Test
    public void onErrorResume2(){
        Flux<String> timeout = Flux.just("timeout")
                .flatMap(this::callExternalService)
                .onErrorResume(original -> Flux.error(new Exception("error occur " + original)));//抛出
        timeout.subscribe(System.out::println);
    }

    @Test
    public void onErrorResume3(){
        Flux<String> timeout = Flux.just("timeout")
                .flatMap(this::callExternalService)
                .onErrorMap(original -> new Exception("error occur " + original));////抛出
        timeout.subscribe(System.out::println);
    }

    @Test
    public void doOnError(){
        AtomicLong count = new AtomicLong();
        Flux<String> flux = Flux.just("unknownKey")
                .flatMap(k ->
                        callExternalService(k)
                                .doOnError(e -> {
                                    count.incrementAndGet();
                                    Logger.getLogger(getClass().getName()).log(Level.INFO, "error occur " + k);
                                })

                );

        flux.subscribe(v-> System.out.println(" RECEIVED " + v), System.out::println);
    }

    @Test
    public void interval() throws InterruptedException {
        Flux<String> flux = Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3)
                        return "tick " + input;
                    throw new RuntimeException("boom");
                })
                .onErrorReturn("error ");
        flux.subscribe(System.out::println);
        TimeUnit.MILLISECONDS.sleep(2100);

    }

    @Test
    public void retry() throws InterruptedException {
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3)
                        return "tick " + input;
                    throw new RuntimeException("boom");
                })
                .retry(1)
                .elapsed()
                .subscribe(System.out::println,System.out::println);
        TimeUnit.MILLISECONDS.sleep(2100);

    }

    @Test
    public void exceptions(){
        Flux<String> converted = Flux.range(1, 10)
                .map(i -> {
                    try {
                        return convert(i);
                    } catch (IOException e) {
                        throw Exceptions.propagate(e);
                    }
                });

        converted.subscribe(
                v -> System.out.println("RECEIVED: " + v),
                e -> {
                    if (Exceptions.unwrap(e) instanceof IOException) {
                        System.out.println("Something bad happened with I/O");
                    } else {
                        System.out.println("Something bad happened");
                    }
                }
        );
    }

    private Flux<String> callExternalService(String key){
        if ("timeout".equals(key))
            return Flux.error(new TimeoutException());
        if ("unknownKey".equals(key)) {
            return Flux.error(new UnsupportedOperationException());
        }
        return Flux.error(new RuntimeException());
    }

    private Flux<String> getFromLocalCache(String key){
        return Flux.just(key);
    }

    private String convert(int i) throws IOException {
        if (i > 3) {
            throw new IOException("boom " + i);
        }
        return "OK " + i;
    }

}
