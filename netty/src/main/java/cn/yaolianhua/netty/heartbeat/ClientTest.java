package cn.yaolianhua.netty.heartbeat;

import cn.yaolianhua.netty.groupchat.GroupChatClient;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-24 16:03
 **/
public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        GroupChatClient client = new GroupChatClient("127.0.0.1",7000);
        client.run();
    }
}
