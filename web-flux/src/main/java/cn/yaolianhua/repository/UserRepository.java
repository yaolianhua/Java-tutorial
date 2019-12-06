package cn.yaolianhua.repository;

import cn.yaolianhua.entity.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 11:13
 **/
public interface UserRepository extends ReactiveMongoRepository<UserEntity,String> {

    Mono<UserEntity> findByUsernameAndDeletedIsFalse(String username);

}
