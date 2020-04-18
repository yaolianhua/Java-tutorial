package cn.yaolianhua.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-05 16:42
 **/
public class NIOServer {
    public static void main(String[] args) throws IOException {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(7000));
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (true){
                SocketChannel socketChannel = serverSocketChannel.accept();
                int read = 0 ;
                while (read != -1)
                {
                    read = socketChannel.read(buffer);
                    buffer.rewind();
                }

            }

        }

    }

}
