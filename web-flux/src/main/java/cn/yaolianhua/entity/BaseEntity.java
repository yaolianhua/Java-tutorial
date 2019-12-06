package cn.yaolianhua.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-06 09:18
 **/
@Getter
@Setter
public class BaseEntity implements Serializable {


    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date updateDate;

    protected Boolean deleted = Boolean.FALSE;

}
