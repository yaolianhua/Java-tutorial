package cn.yaolianhua;

import cn.yaolianhua.entity.ItemEntity;
import cn.yaolianhua.entity.OrderEntity;
import cn.yaolianhua.entity.UserEntity;
import cn.yaolianhua.service.ItemService;
import cn.yaolianhua.service.OrderService;
import cn.yaolianhua.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-06 11:55
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = WebFluxApplication.class)
public class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;

    @Test
    public void insertOrder(){
        List<ItemEntity> items = itemService.findAll().buffer().blockFirst();
        assertTrue(!CollectionUtils.isEmpty(items));
        assertTrue(items.size() > 1);
        List<UserEntity> users = userService.findAll().buffer().blockFirst();
        assertTrue(!CollectionUtils.isEmpty(users));

        users.forEach(user ->{

            Mono<OrderEntity> mono = orderService.insertOrder(new OrderEntity(user.getSex() == 1 ? items.subList(0,items.size() - 1) : items, user));
            /**
             * Target bean of type java.util.ArrayList is not of type of the persistent entity (cn.yaolianhua.entity.ItemEntity)!: java.util.ArrayList))
             *
             * Versions 2.1.2.RELEASE up to 2.1.9.RELEASE all work fine.
             * But changing into 2.2.0.RELEASE or 2.2.1.RELEASE, the test fails again
             */

            StepVerifier.create(mono)
                    .assertNext(Assert::assertNotNull)
                    .expectComplete()
                    .verify();
        });

    }

    @Test
    public void find(){
        List<OrderEntity> list = orderService.findAll().buffer().blockFirst();
        assertTrue(!CollectionUtils.isEmpty(list));

        Mono<OrderEntity> orderById = orderService.findOrderById(list.get(0).getId());
        StepVerifier.create(orderById)
                .assertNext(Assert::assertNotNull)
                .expectComplete()
                .verify();

        list.forEach(order ->{
            System.out.printf("\n** order Id %s **\n",order.getId());
            System.out.println("price " + order.getPrice());
            System.out.println("address " + order.getUser().getAddress());
            System.out.println("username " + order.getUser().getUsername());
            System.out.println("items");
            order.getItems().forEach(System.out::println);
            System.out.println();
        });

    }

}
