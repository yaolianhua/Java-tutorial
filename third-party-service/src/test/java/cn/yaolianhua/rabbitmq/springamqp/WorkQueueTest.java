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
@ActiveProfiles(profiles = {"work-queues","send","receiver","rabbitmq"})
public class WorkQueueTest {

    @Test
    public void test(){
/* output
Ready ... running for 10000 ms
[x] Sent Hello.1
[x] Sent Hello..2
Instance 2 [x] received Hello..2
Instance 1 [x] Done in 2.005 s
Instance 1 [x] received Hello...11
[x] Sent Hello...3
Instance 2 [x] Done in 2.004 s
[x] Sent Hello....4
Instance 2 [x] received Hello....4
[x] Sent Hello.5
Instance 1 [x] Done in 3.011 s
Instance 1 [x] received Hello....12
[x] Sent Hello..6
[x] Sent Hello...7
Instance 2 [x] Done in 4.01 s
[x] Sent Hello....8
Instance 2 [x] received Hello..6
[x] Sent Hello.9
Instance 1 [x] Done in 4.012 s
Instance 1 [x] received Hello.13
Instance 2 [x] Done in 2.006 s
[x] Sent Hello..10
Instance 2 [x] received Hello....8
Instance 1 [x] Done in 1.004 s
Instance 1 [x] received Hello..14
This app uses Spring Profiles to control its behavior.

[x] Sent Hello...11
[x] Sent Hello....12
Instance 1 [x] Done in 2.001 s
Instance 1 [x] received Hello...15
[x] Sent Hello.13
Instance 2 [x] Done in 4.009 s
[x] Sent Hello..14
Instance 2 [x] received Hello..10
[x] Sent Hello...15
Instance 1 [x] Done in 3.008 s
Instance 1 [x] received Hello....16
Instance 2 [x] Done in 2.004 s
Instance 2 [x] received Hello...11
[x] Sent Hello....16
[x] Sent Hello.17
[x] Sent Hello..18
Instance 2 [x] Done in 3.005 s
Instance 2 [x] received Hello....12
[x] Sent Hello...19
Instance 1 [x] Done in 4.014 s
[x] Sent Hello....20
 */
    }
}
