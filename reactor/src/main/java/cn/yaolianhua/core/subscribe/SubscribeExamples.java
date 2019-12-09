package cn.yaolianhua.core.subscribe;

import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-07 13:47
 **/
public class SubscribeExamples {
    @Test
    public void subscribe(){
        Flux<Integer> range = Flux.range(1, 3);
        range.subscribe();//以最简单的方式订阅

        range.subscribe(System.out::println);
    }

    @Test
    public void subscribeWithError(){
        Flux<Integer> range =
                Flux.range(1, 4)
                    .map(integer -> {
                            if (integer < 4)
                                return integer;
                        throw new RuntimeException("Got 4");
                    });
        range.subscribe(System.out::println,error -> System.err.println("Error " + error));

    }

    @Test
    public void subscribeWithRunnable(){
        //错误信号和完成信号都是终端事件，并且彼此互斥（您永远不会都得到）
        // 为了使完成消费者工作，我们必须注意不要触发错误
        Flux<Integer> range = Flux.range(1, 4);
        range.subscribe(System.out::println,
                error -> System.err.println("Error " + error),
                ()-> System.out.println("Done")
        );

    }
    @Test
    public void subscribeWithSubscription(){
        //当我们订阅时，我们会收到Subscription。表示我们要从源头获取10个元素（实际上将发出4个元素并完成）
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                sub -> sub.request(10));
    }


    @Test
    public void baseSubscribe(){
        BaseSubscriberExample<Integer> ss = new BaseSubscriberExample<>();
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                s -> s.request(10));
        ints.subscribe(ss);
    }

    @Test
    public void backPressure(){
        Flux.range(1, 10)
                .doOnRequest(r -> System.out.println("request of " + r))
                .subscribe(new BaseSubscriber<Integer>() {

                    @Override
                    public void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    public void hookOnNext(Integer integer) {
                        System.out.println("Cancelling after having received " + integer);
                        cancel();
                    }
                });
    }


}
