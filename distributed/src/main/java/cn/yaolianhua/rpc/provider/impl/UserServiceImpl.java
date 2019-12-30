package cn.yaolianhua.rpc.provider.impl;

import cn.yaolianhua.rpc.common.pojo.UserEntity;
import cn.yaolianhua.rpc.common.service.UserService;
import cn.yaolianhua.rpc.framework.Service;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-25 15:06
 **/
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserEntity findUser(Long id) {
        if (id == null)
            throw new IllegalArgumentException("invalid param id");
        return id == 1 ? new UserEntity(1L,"user",27,"深圳市南山区创业壹号") : null;
    }
}
