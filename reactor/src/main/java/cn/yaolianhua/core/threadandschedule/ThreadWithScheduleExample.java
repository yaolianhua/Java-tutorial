package cn.yaolianhua.core.threadandschedule;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-08 19:27
 **/
public class ThreadWithScheduleExample {

    @Test
    public void monoInThread() throws InterruptedException {
        Mono<String> mono = Mono.just("hello ");
        System.out.println("create mono in " + Thread.currentThread().getName());
        Thread thread = new Thread(() -> mono
                .map(h -> h + "thread ")
                .subscribe(msg -> System.out.println(msg + Thread.currentThread().getName())));

        thread.start();
        thread.join();

    }

    @Test
    public void publishOn(){
        Scheduler scheduler = Schedulers.newParallel("parallel-scheduler", 4);//第二个map在Thread上运行
        final Flux<String> flux = Flux
                .range(1, 2)
                .map(i -> i + 10)
                .publishOn(scheduler)
                .map(v -> "value" + v);

        new Thread(() -> flux.subscribe(System.out::println));//第一个map在匿名线程上运行
    }

    @Test
    public void subscribeOn(){
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);//第一个map运行在这四个线程之一上

        final Flux<String> flux = Flux
                .range(1, 2)
                .map(i -> 10 + i)
                .subscribeOn(s)
                .map(i -> "value " + i);//第二个map也运行在同一线程上

        new Thread(() -> flux.subscribe(System.out::println));
    }

}
