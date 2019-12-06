package cn.yaolianhua.controller;

import cn.yaolianhua.entity.UserEntity;
import cn.yaolianhua.service.UserService;
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
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public Flux<UserEntity> findAllUser(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<UserEntity> findUserById(@PathVariable String id){
        return userService.findOneById(id);
    }

    @GetMapping("/find")
    public Mono<UserEntity> findUserByUsername(String username){
        return userService.findOneByUsername(username);
    }

    @DeleteMapping("/{id}")
    public Mono<Boolean> deleteUser(@PathVariable String id){
        return userService.deleteOne(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<UserEntity>> saveUser(@RequestBody UserEntity user){
        return userService.insertUser(user)
                .flatMap(
                        userEntity -> Mono
                                .just(ResponseEntity.ok(userEntity))
                );
    }

    @PutMapping("/{id}")
    public Mono<UserEntity> updateUser(@RequestBody UserEntity user, @PathVariable String id){
        return userService.updateUser(id,user);
    }




}
