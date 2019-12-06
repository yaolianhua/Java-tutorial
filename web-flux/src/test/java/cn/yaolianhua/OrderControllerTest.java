package cn.yaolianhua;

import cn.yaolianhua.entity.ItemEntity;
import cn.yaolianhua.entity.OrderEntity;
import cn.yaolianhua.service.ItemService;
import cn.yaolianhua.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-05 21:37
 *
 * run Test method one by one
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = WebFluxApplication.class)
public class OrderControllerTest {

//    @Autowired
//    private WebTestClient client;
    private static final String URI_PREFIX = "/api/v1/order";
    private WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    @Test
    public void find(){
        Flux<OrderEntity> flux = webClient.get()
                .uri(URI_PREFIX + "/list")
                .retrieve()
                .bodyToFlux(OrderEntity.class);
        List<OrderEntity> list = flux.buffer().blockFirst();
        assertTrue(!CollectionUtils.isEmpty(list));

        list.forEach(System.out::println);


    }

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Test
    public void saveOrder(){
        List<ItemEntity> items = itemService.findAll().buffer().blockFirst();
        assertTrue(!CollectionUtils.isEmpty(items));
        userService.findAll().toStream().forEach(user ->{

            Mono<OrderEntity> mono = webClient.post()
                    .uri(URI_PREFIX + "/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .syncBody(new OrderEntity(items,user))
                    .retrieve()
                    .bodyToMono(OrderEntity.class);

            StepVerifier.create(mono)
                    .assertNext(Assert::assertNotNull)
                    .expectComplete()
                    .verify();
        });



    }



}
