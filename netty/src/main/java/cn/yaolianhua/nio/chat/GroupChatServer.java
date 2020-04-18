package cn.yaolianhua.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-05 14:57
 **/
public class GroupChatServer {
    public static void main(String[] args) throws IOException {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private static final int PORT = 7000;

    public GroupChatServer() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void listen() throws IOException {
        for (;;){
            int count = selector.select();//blocking
            if (count > 0){//has event
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()){
                    SelectionKey selectionKey = keyIterator.next();
                    if (selectionKey.isAcceptable()){//OP_ACCEPT
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                        System.out.println(socketChannel.getRemoteAddress() + " online");
                    }
                    if (selectionKey.isReadable()){//OP_READ
                        read(selectionKey);
                    }
                    keyIterator.remove();
                }
            }else
                System.out.println("waiting client connected");
        }

    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(buffer);
        if (read > 0){
            String msg = new String(buffer.array()).trim();
            System.out.println("From Client " + socketChannel.getRemoteAddress() + ": " + msg);
            sendMsgToOtherClients(msg,socketChannel);
        }
    }

    private void sendMsgToOtherClients(String msg,SocketChannel self){
        System.out.println("Server Forward msg ...");
        selector.keys()
                .stream()
                .filter(key -> (key.channel() instanceof SocketChannel) && key.channel() != self)
                .map(key -> (SocketChannel)key.channel())
                .forEach(sc -> {
                    try {
                        sc.write(ByteBuffer.wrap(msg.getBytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
