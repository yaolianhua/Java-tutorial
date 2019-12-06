package cn.yaolianhua.controller;


import cn.yaolianhua.entity.ItemEntity;
import cn.yaolianhua.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 22:01
 **/
@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/list")
    public Flux<ItemEntity> findAllItem(){
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ItemEntity> findItemById(@PathVariable String id){
        return itemService.findOneById(id);
    }

    @GetMapping("/find")
    public Mono<ItemEntity> findItemByItemName(String itemName){
        return itemService.findByItemName(itemName);
    }

    @GetMapping("/find/{low}/{top}")
    public Flux<ItemEntity> findByPriceBetween(@PathVariable Double low,@PathVariable Double top){
        return itemService.findByPriceBetween(low,top);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ItemEntity>> saveItem(@RequestBody ItemEntity item){
        return itemService.insertItem(item)
                .flatMap(
                        itemEntity -> Mono
                                .just(ResponseEntity.ok(itemEntity))
                );
    }

}
