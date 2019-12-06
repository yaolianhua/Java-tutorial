package cn.yaolianhua.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 21:18
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "item")
public class ItemEntity extends BaseEntity implements Serializable {

    @Id
    private String id;
    private String itemName;
    private Double price;

    public ItemEntity(String itemName, Double price) {
        this.itemName = itemName;
        this.price = price;
    }
}
