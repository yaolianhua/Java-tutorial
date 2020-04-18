package cn.yaolianhua.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-05 13:23
 **/
public class NIOClient {

    public static void main(String[] args) throws IOException {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 7000);
            socketChannel.configureBlocking(false);
            if (!socketChannel.connect(inetSocketAddress)) {
                while (!socketChannel.finishConnect()){
                    System.out.println("Before the client connected success,you can do something else");
                }

            }

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String message = scanner.nextLine();
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                socketChannel.write(buffer);

            }



        }
    }
}
