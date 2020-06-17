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
@ActiveProfiles(profiles = {"pub-sub","send","receiver","rabbitmq"})
public class PubSubTest {

    @Test
    public void test(){
/* output
Ready ... running for 10000 ms
[x] Sent Hello.1
Instance 1  [x] Received Hello.1
Instance 2  [x] Received Hello.1
[x] Sent Hello..2
Instance 2  [x] Done in 1.004 s
Instance 1  [x] Done in 1.005 s
Instance 1  [x] Received Hello..2
Instance 2  [x] Received Hello..2
[x] Sent Hello...3
[x] Sent Hello.4
Instance 2  [x] Done in 2.005 s
Instance 1  [x] Done in 2.005 s
Instance 2  [x] Received Hello...3
Instance 1  [x] Received Hello...3
[x] Sent Hello..5
[x] Sent Hello...6
[x] Sent Hello.7
Instance 2  [x] Done in 3.006 s
Instance 1  [x] Done in 3.006 s
Instance 1  [x] Received Hello.4
Instance 2  [x] Received Hello.4
[x] Sent Hello..8
Instance 1  [x] Done in 1.002 s
Instance 2  [x] Done in 1.002 s
Instance 2  [x] Received Hello..5
Instance 1  [x] Received Hello..5
[x] Sent Hello...9
[x] Sent Hello.10
Instance 2  [x] Done in 2.004 s
Instance 1  [x] Done in 2.004 s
Instance 1  [x] Received Hello...6
Instance 2  [x] Received Hello...6
This app uses Spring Profiles to control its behavior.

[x] Sent Hello..11
[x] Sent Hello...12
[x] Sent Hello.13
Instance 1  [x] Done in 3.011 s
Instance 2  [x] Done in 3.011 s
Instance 1  [x] Received Hello.7
Instance 2  [x] Received Hello.7
[x] Sent Hello..14
Instance 1  [x] Done in 1.004 s
Instance 2  [x] Done in 1.004 s
Instance 2  [x] Received Hello..8
Instance 1  [x] Received Hello..8
[x] Sent Hello...15
[x] Sent Hello.16
Instance 1  [x] Done in 2.005 s
Instance 2  [x] Done in 2.005 s
Instance 2  [x] Received Hello...9
[x] Sent Hello..17
[x] Sent Hello...18
[x] Sent Hello.19
Instance 2  [x] Done in 3.01 s
Instance 2  [x] Received Hello.10
[x] Sent Hello..20
Instance 2  [x] Done in 1.004 s
Instance 2  [x] Received Hello..11
 */
    }
}
