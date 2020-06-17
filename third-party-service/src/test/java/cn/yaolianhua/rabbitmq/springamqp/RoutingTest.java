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
@ActiveProfiles(profiles = {"routing","send","receiver","rabbitmq"})
public class RoutingTest {

    @Test
    public void test(){
/* output
Ready ... running for 10000 ms
[x] Sent Hello to black 1
Instance 2  [x] Received Hello to black 1
Instance 1  [x] Received Hello to black 1
Instance 2  [x] Done in 0.0 s
Instance 1  [x] Done in 0.0 s
[x] Sent Hello to green 2
Instance 2  [x] Received Hello to green 2
Instance 2  [x] Done in 0.0 s
[x] Sent Hello to orange 3
Instance 1  [x] Received Hello to orange 3
Instance 1  [x] Done in 0.001 s
[x] Sent Hello to black 4
Instance 2  [x] Received Hello to black 4
Instance 1  [x] Received Hello to black 4
Instance 2  [x] Done in 0.0 s
Instance 1  [x] Done in 0.0 s
[x] Sent Hello to green 5
Instance 2  [x] Received Hello to green 5
Instance 2  [x] Done in 0.0 s
[x] Sent Hello to orange 6
Instance 1  [x] Received Hello to orange 6
Instance 1  [x] Done in 0.0 s
[x] Sent Hello to black 7
Instance 2  [x] Received Hello to black 7
Instance 1  [x] Received Hello to black 7
Instance 2  [x] Done in 0.0 s
Instance 1  [x] Done in 0.0 s
[x] Sent Hello to green 8
Instance 2  [x] Received Hello to green 8
Instance 2  [x] Done in 0.0 s
[x] Sent Hello to orange 9
Instance 1  [x] Received Hello to orange 9
Instance 1  [x] Done in 0.0 s
[x] Sent Hello to black 10
Instance 2  [x] Received Hello to black 10
Instance 2  [x] Done in 0.0 s
Instance 1  [x] Received Hello to black 10
Instance 1  [x] Done in 0.0 s
This app uses Spring Profiles to control its behavior.

[x] Sent Hello to green 11
Instance 2  [x] Received Hello to green 11
Instance 2  [x] Done in 0.0 s
[x] Sent Hello to orange 12
 */
    }
}
