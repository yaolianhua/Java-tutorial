package cn.yaolianhua.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 21:17
 **/

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "order")
@Getter
@Setter
public class OrderEntity extends BaseEntity implements Serializable {

    @Id
    private String id;
    private UserEntity user;
    private List<ItemEntity> items;
    private Double price;

    public OrderEntity(List<ItemEntity> items,UserEntity user) {
        this.user = user;
        this.items = items;
        this.price = items.stream().mapToDouble(ItemEntity::getPrice).reduce(0.00, ((left, right) -> left + right));
    }

}
