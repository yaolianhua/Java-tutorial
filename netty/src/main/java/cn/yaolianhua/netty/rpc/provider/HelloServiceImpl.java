package cn.yaolianhua.netty.rpc.provider;

import cn.yaolianhua.netty.rpc.HelloService;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-27 10:50
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        return "HelloServiceImpl reply: " + msg;
    }
}
