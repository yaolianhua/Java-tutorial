package cn.yaolianhua;

import cn.yaolianhua.entity.UserEntity;
import cn.yaolianhua.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 10:05
 *
 * run Test method one by one
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = WebFluxApplication.class)
public class UserServiceTest {

    @Autowired
    UserService userService;
    private static UserEntity[] userEntities = new UserEntity[]{
            new UserEntity("yaolianhua","深圳市南山区石鼓路4006号",27,1),
            new UserEntity("yaozeyong","深圳市南山区石鼓路4006号",60,1),
            new UserEntity("daihuagui","深圳市南山区石鼓路4006号",57,0),
            new UserEntity("guoxiaobing","深圳市南山区石鼓路4006号",27,0),
            new UserEntity("yaolingling","衡阳市蒸湘区碧桂园小区",33,0)
    };
    private static List<String> usernameList = Stream.of(userEntities).map(UserEntity::getUsername).collect(Collectors.toList());


    @Test
    public void saveUserList(){
        for (UserEntity user : userEntities) {
            Mono<UserEntity> mono = userService.insertUser(user);

            StepVerifier.create(mono)
                    .assertNext(userEntity -> assertNotNull(userEntity.getId()))
                    .expectComplete()
                    .verify();
        }
    }

    @Test
    public void findAllUser(){
        userService.findAll()
                .buffer()
                .toStream()
                .findFirst()
                .ifPresent(users -> {
                    boolean containsAll =
                            users
                                    .stream()
                                    .map(UserEntity::getUsername)
                                    .collect(Collectors.toList())
                                    .containsAll(usernameList);
                    Logger.getLogger(getClass().getName()).log(Level.INFO,"findAll() must contains userEntities [" +containsAll+ "]");
                });
    }

    @Test
    public void findOne(){
        UserEntity found = userService.findAll()
                .filter(u -> !u.getDeleted())
                .switchIfEmpty(Flux.error(new Exception("No user Found")))
                .blockFirst();
        assert found != null;
        String id = found.getId();
        String username = found.getUsername();
        Logger.getLogger(getClass().getName()).log(Level.INFO,"Found the first user：Id [" +id+ "] username [" +username+ "]");
        Mono<UserEntity> oneById = userService.findOneById(id);
        Mono<UserEntity> oneByUsername = userService.findOneByUsername(username);

        StepVerifier.create(oneById)
                .assertNext(u -> assertTrue(usernameList.contains(u.getUsername())))
                .expectComplete()
                .verify();
        StepVerifier.create(oneByUsername)
                .assertNext(u->assertTrue(usernameList.indexOf(u.getUsername()) > -1))
                .expectComplete()
                .verify();
        Mono<UserEntity> noExistUsername = userService.findOneByUsername("NoExistUsername");
        StepVerifier.create(noExistUsername)
                .assertNext(Assert::assertNotNull)
                .expectComplete()
                .verify();//java.lang.Exception: No user found with username NoExistUsername
    }

    @Test
    public void deleteUser(){
        UserEntity found = userService.findAll()
                .filter(u -> !u.getDeleted())
                .switchIfEmpty(Flux.error(new Exception("No user Found")))
                .blockFirst();
        assert found != null;
        String id = found.getId();
        Mono<Boolean> deleteOne = userService.deleteOne(id);
        StepVerifier.create(deleteOne)
                .assertNext(Assert::assertTrue)
                .expectComplete()
                .verify();

        Mono<Boolean> noExistId = userService.deleteOne("NoExistId");
        StepVerifier.create(noExistId)
                .assertNext(Assert::assertTrue)
                .expectComplete()
                .verify();
    }

    @Test
    public void updateUser(){
        userService.findAll()
                .toStream()
                .filter(o-> !o.getDeleted())
                .collect(Collectors.toList())
                //update all users deleted
                .forEach(old -> {
                    Mono<UserEntity> updateUser = userService.updateUser(old.getId(), new UserEntity(true));


                    StepVerifier.create(updateUser)
                            .assertNext(u -> assertTrue(u.getDeleted()))
                            .expectComplete()
                            .verify();

                });
    }


}
