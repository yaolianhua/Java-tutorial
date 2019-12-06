package cn.yaolianhua.service;

import cn.yaolianhua.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 22:11
 **/
public interface UserService {

    Mono<UserEntity> insertUser(UserEntity user);

    Mono<UserEntity> updateUser(String id,UserEntity user);

    Flux<UserEntity> findAll();

    Mono<UserEntity> findOneById(String id);

    Mono<UserEntity> findOneByUsername(String username);

    Mono<Boolean> deleteOne(String id);
}
