package cn.yaolianhua.rabbitmq.springamqp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 16:41
 **/
@SpringBootTest(classes = RabbitAmqpApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"rpc","server","client","rabbitmq"})
public class RpcTest {

    @Test
    public void test(){
/*
Ready ... running for 10000 ms
[x] Requesting fib(0)
[x] Received request for 0
[.] Returned 0
[.] Got 0
[x] Requesting fib(1)
[x] Received request for 1
[.] Returned 1
[.] Got 1
[x] Requesting fib(2)
[x] Received request for 2
[.] Returned 1
[.] Got 1
[x] Requesting fib(3)
[x] Received request for 3
[.] Returned 2
[.] Got 2
[x] Requesting fib(4)
[x] Received request for 4
[.] Returned 3
[.] Got 3
[x] Requesting fib(5)
[x] Received request for 5
[.] Returned 5
[.] Got 5
[x] Requesting fib(6)
[x] Received request for 6
[.] Returned 8
[.] Got 8
[x] Requesting fib(7)
[x] Received request for 7
[.] Returned 13
[.] Got 13
[x] Requesting fib(8)
[x] Received request for 8
[.] Returned 21
[.] Got 21
[x] Requesting fib(9)
[x] Received request for 9
[.] Returned 34
[.] Got 34
This app uses Spring Profiles to control its behavior.
 */
    }
}
