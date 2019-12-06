package cn.yaolianhua.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 11:09
 **/
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class UserEntity extends BaseEntity implements Serializable {

    @Id
    private String id;
    private String username;
    private String address;
    private Integer age;
    private Integer sex;


    public UserEntity(String username, String address,Integer age, Integer sex) {
        this.username = username;
        this.address = address;
        this.age = age;
        this.sex = sex;
    }
    public UserEntity(Boolean deleted){
        this.deleted = deleted;
    }

}
