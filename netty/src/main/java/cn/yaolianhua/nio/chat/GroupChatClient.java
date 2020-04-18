package cn.yaolianhua.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-05 15:40
 **/
public class GroupChatClient {
    public static void main(String[] args) throws IOException {
        GroupChatClient chatClient = new GroupChatClient();
        new Thread(()->{
            while (true){
                try {
                    chatClient.read();
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            chatClient.send(scanner.nextLine());
        }
    }

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 7000;
    private final Selector selector;
    private final SocketChannel socketChannel;
    private final String username;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is ok");
    }

    private void send(String msg) throws IOException {
        msg = username + " say: " + msg;
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }

    private void read() throws IOException {
        int select = selector.select();
        if (select > 0){
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()){
                SelectionKey selectionKey = selectionKeyIterator.next();
                if (selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    System.out.println(new String(buffer.array()).trim());
                }
                selectionKeyIterator.remove();
            }
        }else
            System.out.println("No available channel");
    }
}
