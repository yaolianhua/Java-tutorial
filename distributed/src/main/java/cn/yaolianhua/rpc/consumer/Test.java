package cn.yaolianhua.rpc.consumer;

import cn.yaolianhua.rpc.common.service.UserService;
import cn.yaolianhua.rpc.framework.RpcFramework;
import cn.yaolianhua.rpc.framework.ZkRegistryCenter;

import java.io.IOException;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-26 22:34
 **/
public class Test {

    public static void main(String[] args) throws IOException {

        ZkRegistryCenter.registerConsumers("cn.yaolianhua.rpc.consumer","127.0.0.1",6666);
        UserService proxy = RpcFramework.getProxy(UserService.class);
        RpcClient client = new RpcClient(proxy);
        System.out.println(client.findUser(1L));
    }


}
