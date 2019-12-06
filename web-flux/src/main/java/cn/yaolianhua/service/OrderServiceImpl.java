package cn.yaolianhua.service;

import cn.yaolianhua.entity.OrderEntity;
import cn.yaolianhua.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-06 11:50
 **/
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<OrderEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<OrderEntity> findOrderById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("No order found with Id " + id)));
    }

    @Override
    public Mono<OrderEntity> insertOrder(OrderEntity order) {
        return repository.insert(order);
    }
}
