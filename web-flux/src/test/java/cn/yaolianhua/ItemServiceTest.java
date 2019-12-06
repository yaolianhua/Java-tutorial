package cn.yaolianhua;

import cn.yaolianhua.entity.ItemEntity;
import cn.yaolianhua.service.ItemService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-06 11:08
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = WebFluxApplication.class)
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    private static ItemEntity[] itemEntities = new ItemEntity[]{
            new ItemEntity("mate20 Pro", 4999.00),
            new ItemEntity("mate30 Pro", 5999.00),
            new ItemEntity("nova5", 3999.00),
    };

    @Test
    public void insertItem(){
        Stream.of(itemEntities)
                .forEach(item ->{
                    Mono<ItemEntity> mono = itemService.insertItem(item);
                    StepVerifier.create(mono)
                            .assertNext(itemEntity -> assertNotNull(itemEntity.getId()))
                            .expectComplete()
                            .verify();
                });
    }

    @Test
    public void find(){
        List<ItemEntity> blockFirst = itemService.findAll()
                .buffer()
                .blockFirst();
        assertTrue(!CollectionUtils.isEmpty(blockFirst));
        blockFirst.forEach(System.out::println);//all

        Mono<ItemEntity> byItemName = itemService.findByItemName(blockFirst.get(0).getItemName());
        Mono<ItemEntity> oneById = itemService.findOneById(blockFirst.get(0).getId());
        StepVerifier.create(byItemName)
                .assertNext(itemEntity -> assertEquals("mate20 Pro",itemEntity.getItemName()))
                .expectComplete()
                .verify();
        StepVerifier.create(oneById)
                .assertNext(Assert::assertNotNull)
                .expectComplete()
                .verify();

        Flux<ItemEntity> byPriceBetween = itemService.findByPriceBetween(5000.0, 6000.0);
        StepVerifier.create(byPriceBetween)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }



}
