package cn.yaolianhua.rabbitmq.springamqp.rpc;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 23:09
 **/
public class Server {
    @RabbitListener(queues = "rpc_queue")
//     @SendTo("tut.rpc.replies") //used when the client doesn't set replyTo.
    public int fibonacci(int n) {
        System.out.println(String.format("[x] Received request for %s",n));
        int result = fib(n);
        System.out.println(String.format("[.] Returned %s",result));
        return result;
    }

    public int fib(int n) {
        if (n == 0) return 0;
        return n == 1 ? 1 : (fib(n - 1) + fib(n - 2));
    }
}
