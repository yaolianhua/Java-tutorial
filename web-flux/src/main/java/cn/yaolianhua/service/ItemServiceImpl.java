package cn.yaolianhua.service;

import cn.yaolianhua.entity.ItemEntity;
import cn.yaolianhua.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-06 10:37
 **/
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    public ItemServiceImpl(ItemRepository repository) {
        this.repository = repository;
    }


    @Override
    public Mono<ItemEntity> insertItem(ItemEntity item) {
        return repository.insert(item);
    }

    @Override
    public Mono<ItemEntity> findOneById(String id) {
        return repository.findById(id)
                .filter(itemEntity -> !itemEntity.getDeleted())
                .switchIfEmpty(Mono.error(new Exception("No item found with Id " + id)));
    }

    @Override
    public Flux<ItemEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Flux<ItemEntity> findByPriceBetween(Double low, Double top) {
        return repository.findByPriceBetweenAndDeletedIsFalse(low, top)
                .switchIfEmpty(Flux.error(new Exception("No item found with price between "
                        + low + " ~ " + top)));
    }

    @Override
    public Mono<ItemEntity> findByItemName(String itemName) {
        return repository.findByItemNameAndDeletedIsFalse(itemName)
                .switchIfEmpty(Mono.error(new Exception("No item found with itemName " + itemName)));
    }
}
