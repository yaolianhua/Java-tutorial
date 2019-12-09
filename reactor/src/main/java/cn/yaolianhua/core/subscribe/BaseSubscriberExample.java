package cn.yaolianhua.core.subscribe;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-07 14:41
 **/
public class BaseSubscriberExample<T> extends BaseSubscriber<T> {

    public void hookOnSubscribe(Subscription subscription) {
        System.out.println("Subscribed");
        request(1);
    }

    public void hookOnNext(T value) {
        System.out.println(value);
        request(1);
    }
}
