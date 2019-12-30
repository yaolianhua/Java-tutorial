package cn.yaolianhua.rpc.common.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-25 14:56
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntity implements Serializable {
    private Long id;
    private String name;
    private Integer age;
    private String address;
}
