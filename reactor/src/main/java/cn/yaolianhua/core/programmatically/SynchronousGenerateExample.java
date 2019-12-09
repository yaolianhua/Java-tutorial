package cn.yaolianhua.core.programmatically;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-07 19:38
 **/
public class SynchronousGenerateExample {

    @Test
    public void generateWithStateBase(){
        Flux<String> flux = Flux.generate(
                () -> 1,//提供初始状态值1
                (state, sink) -> {
                    sink.next(state + " x " + state + " = " + state * state);
                    if (state == 10)
                        sink.complete();
                    return state + 1;

                });

        flux.subscribe(System.out::println);
    }

    @Test
    public void generateWithMutableStateVariant(){
        Flux<String> flux = Flux.generate(
                AtomicLong::new,//生成一个可变对象作为状态
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next(state.get() + " x " + state.get() + " = " + state.get() * state.get());
                    if (i == 10) sink.complete();
                    return state;
                });
        flux.subscribe(System.out::println);
    }

    @Test
    public void generateWithConsumer(){
        Flux<String> flux = Flux.generate(
                AtomicLong::new,//生成一个可变对象作为状态
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next(state.get() + " x " + state.get() + " = " + state.get() * state.get());
                    if (i == 10) sink.complete();
                    return state;
                },
                //如果状态包含在过程结束时需要处理的数据库连接或其他资源，
                // 则Consumer可以关闭连接或以其他方式处理应在过程结束时完成的任何任务
                state -> System.out.println("state :" + state));
        flux.subscribe(System.out::println);
    }
}
