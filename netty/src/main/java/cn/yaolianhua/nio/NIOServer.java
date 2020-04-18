package cn.yaolianhua.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-05 12:24
 **/
public class NIOServer {

    public static void main(String[] args) throws IOException {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            try (Selector selector = Selector.open()) {//get Selector
                serverSocketChannel.socket().bind(new InetSocketAddress(7000));
                serverSocketChannel.configureBlocking(false);//unblocking
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

                for (;;){
                    if (selector.select(6000) == 0){//blocking 6s
                        System.out.println("Server wait 6s with no event occurred");
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();//get selected-key set
                    while (keyIterator.hasNext()){
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isAcceptable()){//OP_ACCEPT
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            System.out.println("Client connected success,socketChannel " + socketChannel.hashCode());
                            socketChannel.configureBlocking(false);
                            //register the socketChannel to selector and allocate buffer for this socketChannel
                            socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        }
                        if (selectionKey.isReadable()){//OP_READ
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                            channel.read(buffer);//read data from socketChannel to buffer
                            System.out.println("From Client: " + new String(buffer.array()).trim());
                        }
                        keyIterator.remove();

                    }


                }

            }

        }

    }

}
