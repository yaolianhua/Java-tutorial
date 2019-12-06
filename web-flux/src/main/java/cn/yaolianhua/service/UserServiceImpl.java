package cn.yaolianhua.service;

import cn.yaolianhua.entity.UserEntity;
import cn.yaolianhua.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 22:17
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserEntity> insertUser(UserEntity user) {
        return userRepository.insert(user);
    }

    @Override
    public Mono<UserEntity> updateUser(String id, UserEntity user) {
        return findOneById(id).doOnSuccess(oldUser->{
            oldUser.setUsername(StringUtils.isEmpty(user.getUsername()) ? oldUser.getUsername() : user.getUsername());
            oldUser.setAddress(StringUtils.isEmpty(user.getAddress()) ? oldUser.getAddress() : user.getAddress());
            oldUser.setAge(user.getAge() == null ? oldUser.getAge() : user.getAge());
            oldUser.setSex(user.getSex() == null ? oldUser.getSex() : user.getSex());
            oldUser.setDeleted(user.getDeleted() == null ? oldUser.getDeleted() : user.getDeleted());
            userRepository.save(oldUser).subscribe();
        });
    }

    @Override
    public Flux<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<UserEntity> findOneById(String id) {
        return userRepository.findById(id).filter(user -> !user.getDeleted())
                .switchIfEmpty(Mono.error(new Exception("No user found with Id " + id)));
    }

    @Override
    public Mono<UserEntity> findOneByUsername(String username) {
        return userRepository.findByUsernameAndDeletedIsFalse(username)
                .switchIfEmpty(Mono.error(new Exception("No user found with username " + username)));
    }

    @Override
    public Mono<Boolean> deleteOne(String id) {
        return findOneById(id).doOnSuccess(old->{
            old.setDeleted(true);
            userRepository.save(old).subscribe();
        }).flatMap(user ->Mono.just(Boolean.TRUE));
    }
}
