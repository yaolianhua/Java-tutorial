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
@ActiveProfiles(profiles = {"topic","send","receiver","rabbitmq"})
public class TopicTest {

    @Test
    public void test(){
/* output
Ready ... running for 10000 ms
[x] Sent Hello to lazy.orange.elephant 1
Instance 1  [x] Received Hello to lazy.orange.elephant 1
Instance 2  [x] Received Hello to lazy.orange.elephant 1
[x] Sent Hello to quick.orange.fox 2
[x] Sent Hello to lazy.brown.fox 3
Instance 1  [x] Done in 2.004 s
Instance 2  [x] Done in 2.004 s
Instance 2  [x] Received Hello to lazy.brown.fox 3
Instance 1  [x] Received Hello to quick.orange.fox 2
[x] Sent Hello to lazy.pink.rabbit 4
[x] Sent Hello to quick.brown.fox 5
Instance 1  [x] Done in 2.005 s
Instance 2  [x] Done in 2.005 s
Instance 2  [x] Received Hello to lazy.pink.rabbit 4
Instance 1  [x] Received Hello to lazy.pink.rabbit 4
[x] Sent Hello to quit.orange.rabbit 6
[x] Sent Hello to lazy.orange.elephant 7
Instance 2  [x] Done in 2.004 s
Instance 1  [x] Done in 2.004 s
Instance 2  [x] Received Hello to lazy.orange.elephant 7
Instance 1  [x] Received Hello to quit.orange.rabbit 6
[x] Sent Hello to quick.orange.fox 8
[x] Sent Hello to lazy.brown.fox 9
Instance 2  [x] Done in 2.006 s
Instance 1  [x] Done in 2.006 s
Instance 1  [x] Received Hello to lazy.orange.elephant 7
Instance 2  [x] Received Hello to lazy.brown.fox 9
[x] Sent Hello to lazy.pink.rabbit 10
This app uses Spring Profiles to control its behavior.

[x] Sent Hello to quick.brown.fox 11
Instance 2  [x] Done in 2.005 s
Instance 1  [x] Done in 2.005 s
Instance 2  [x] Received Hello to lazy.pink.rabbit 10
Instance 1  [x] Received Hello to quick.orange.fox 8
[x] Sent Hello to quit.orange.rabbit 12
[x] Sent Hello to lazy.orange.elephant 13
Instance 1  [x] Done in 2.002 s
Instance 2  [x] Done in 2.002 s
Instance 1  [x] Received Hello to lazy.pink.rabbit 10
Instance 2  [x] Received Hello to lazy.orange.elephant 13
[x] Sent Hello to quick.orange.fox 14
Instance 1  [x] Done in 2.005 s
[x] Sent Hello to lazy.brown.fox 15
Instance 2  [x] Done in 2.007 s
Instance 2  [x] Received Hello to lazy.brown.fox 15
[x] Sent Hello to lazy.pink.rabbit 16
[x] Sent Hello to quick.brown.fox 17
Instance 2  [x] Done in 2.001 s
 */
    }
}
