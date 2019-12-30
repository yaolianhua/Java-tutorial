package cn.yaolianhua.rpc.common.service;

import cn.yaolianhua.rpc.common.pojo.UserEntity;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-25 14:55
 **/
public interface UserService {
    UserEntity findUser(Long id);
}
