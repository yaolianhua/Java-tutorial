package cn.yaolianhua.service;

import cn.yaolianhua.entity.OrderEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-06 11:48
 **/
public interface OrderService {

    Mono<OrderEntity> findOrderById(String id);

    Mono<OrderEntity> insertOrder(OrderEntity order);

    Flux<OrderEntity> findAll();

}
