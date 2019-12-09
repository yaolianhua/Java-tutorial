package cn.yaolianhua.core.programmatically;

import org.junit.Test;
import reactor.core.publisher.Flux;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-08 18:53
 **/
public class HandleExample {

    @Test
    public void handle(){
        Flux<Object> handle = Flux.just(-1, 30, 13, 9, 20)
                .handle(((integer, synchronousSink) -> {
                    String alphabet = alphabet(integer);
                    if (alphabet != null)
                        synchronousSink.next(alphabet);
                }));

        handle.subscribe(System.out::println);
    }

    private String alphabet(int letterNumber){
        if (letterNumber < 1 || letterNumber > 26) {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + ((char) letterIndexAscii);
    }
}
