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
@ActiveProfiles(profiles = {"hello-world","send","receiver","rabbitmq"})
public class HelloWorldTest {

    @Test
    public void test(){

    }
}
