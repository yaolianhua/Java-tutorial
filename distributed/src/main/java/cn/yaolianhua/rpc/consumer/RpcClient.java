package cn.yaolianhua.rpc.consumer;

import cn.yaolianhua.rpc.common.pojo.UserEntity;
import cn.yaolianhua.rpc.common.service.UserService;
import cn.yaolianhua.rpc.framework.Reference;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-26 11:43
 **/
public class RpcClient {

    @Reference
    private UserService userService;

    public RpcClient(UserService userService) {
        this.userService = userService;
    }

    public UserEntity findUser(Long id){
        return userService.findUser(id);
    }
}
