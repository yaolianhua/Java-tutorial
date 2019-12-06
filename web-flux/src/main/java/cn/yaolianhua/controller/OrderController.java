package cn.yaolianhua.controller;

import cn.yaolianhua.entity.OrderEntity;
import cn.yaolianhua.service.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-06 17:06
 **/
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Mono<OrderEntity> findById(@PathVariable String id){
        return orderService.findOrderById(id);
    }

    @PostMapping("/save")
    public Mono<OrderEntity> saveItem(@RequestBody OrderEntity order){
        return orderService.insertOrder(order);
    }

    @GetMapping("/list")
    public Flux<OrderEntity> findAll(){
        return orderService.findAll();
    }


}
