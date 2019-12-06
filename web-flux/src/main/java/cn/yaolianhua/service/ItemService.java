package cn.yaolianhua.service;

import cn.yaolianhua.entity.ItemEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-06 10:33
 **/
public interface ItemService {

    Mono<ItemEntity> insertItem(ItemEntity item);

    Mono<ItemEntity> findOneById(String id);

    Flux<ItemEntity> findAll();

    Flux<ItemEntity> findByPriceBetween(Double low,Double top);

    Mono<ItemEntity> findByItemName(String itemName);


}
