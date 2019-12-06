package cn.yaolianhua.repository;

import cn.yaolianhua.entity.OrderEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 21:28
 **/
public interface OrderRepository extends ReactiveMongoRepository<OrderEntity,String> {

}
