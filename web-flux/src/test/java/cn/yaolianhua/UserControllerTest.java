package cn.yaolianhua;

import cn.yaolianhua.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-05 21:37
 *
 * run Test method one by one
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = WebFluxApplication.class)
public class UserControllerTest {

//    @Autowired
//    private WebTestClient client;
    private static final String URI_PREFIX = "/api/v1/user";
    private WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    @Test
    public void saveUser(){
        Mono<UserEntity> mono = webClient.post()
                .uri(URI_PREFIX + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(new UserEntity("ylh","深圳市南山区创业壹号",27,1))
                .retrieve()
                .bodyToMono(UserEntity.class);
        assertEquals("ylh", Objects.requireNonNull(mono.block()).getUsername());

    }
    @Test
    public void updateUser(){
        UserEntity entity = webClient.method(HttpMethod.GET)
                .uri(URI_PREFIX + "/find?username={username}", "ylh")
                .retrieve()
                .onStatus(HttpStatus::isError, res -> Mono.error(() -> new Exception("unexpected error")))
                .bodyToMono(UserEntity.class)
                .block();
        assert entity != null;

        UserEntity userEntity = webClient.put()
                .uri(URI_PREFIX + "/"+entity.getId())
                .syncBody(new UserEntity( "yaolh", "深圳市南山区创业壹号", 27, 1))
                .retrieve()
                .bodyToMono(UserEntity.class)
                .block();
        assert userEntity != null;
        assertEquals("yaolh",userEntity.getUsername());
    }

    @Test
    public void findUserByUsername(){
        UserEntity entity = webClient.method(HttpMethod.GET)
                .uri(URI_PREFIX + "/find?username={username}", "yaolh")
                .retrieve()
                .onStatus(HttpStatus::isError, res -> Mono.error(() -> new Exception("unexpected error")))
                .bodyToMono(UserEntity.class)
                .block();
        assertNotNull(entity);

    }

    @Test
    public void findUserById(){
        UserEntity u = webClient.method(HttpMethod.GET)
                .uri(URI_PREFIX + "/find?username={username}", "yaolh")
                .retrieve()
                .onStatus(HttpStatus::isError, res -> Mono.error(() -> new Exception("unexpected error")))
                .bodyToMono(UserEntity.class)
                .block();
        assert u != null;
        UserEntity entity = webClient.method(HttpMethod.GET)
                .uri(URI_PREFIX + "/"+u.getId())
                .retrieve()
                .onStatus(HttpStatus::isError, res -> Mono.error(() -> new Exception("unexpected error")))
                .bodyToMono(UserEntity.class)
                .block();
        assertNotNull(entity);

    }

    @Test
    public void findAllUser(){
        Flux<List<UserEntity>> flux = webClient.get()
                .uri(URI_PREFIX + "/list")
                .retrieve()
                .bodyToFlux(UserEntity.class)
                .doOnError(e -> System.out.println(e.getMessage()))
                .buffer();
        List<UserEntity> list = flux.blockFirst();
        assertTrue(!CollectionUtils.isEmpty(list));
        list.forEach(System.out::println);
    }

    @Test
    public void deleteUser(){
        UserEntity u = webClient.method(HttpMethod.GET)
                .uri(URI_PREFIX + "/find?username={username}", "yaolh")
                .retrieve()
                .onStatus(HttpStatus::isError, res -> Mono.error(() -> new Exception("unexpected error")))
                .bodyToMono(UserEntity.class)
                .block();
        assert u != null;
        Boolean aBoolean = webClient.delete()
                .uri(URI_PREFIX + "/"+u.getId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        assertEquals(true, aBoolean);
    }
}
