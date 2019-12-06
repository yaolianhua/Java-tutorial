package cn.yaolianhua.repository;

import cn.yaolianhua.entity.ItemEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 21:28
 **/
public interface ItemRepository extends ReactiveMongoRepository<ItemEntity,String> {

    Flux<ItemEntity> findByPriceBetweenAndDeletedIsFalse(Double low,Double top);

    Mono<ItemEntity> findByItemNameAndDeletedIsFalse(String itemName);
}
