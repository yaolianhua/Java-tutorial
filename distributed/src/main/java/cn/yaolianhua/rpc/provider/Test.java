package cn.yaolianhua.rpc.provider;

import cn.yaolianhua.rpc.framework.RpcFramework;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-26 22:24
 **/
public class Test {
    public static void main(String[] args) throws Exception {
        RpcFramework.startProvider("cn.yaolianhua.rpc.provider","127.0.0.1",6666);
    }

}
