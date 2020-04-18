package cn.yaolianhua.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-04 15:36
 **/
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(7000));

            ByteBuffer[] byteBuffers = new ByteBuffer[2];
            byteBuffers[0] = ByteBuffer.allocate(5);
            byteBuffers[1] = ByteBuffer.allocate(3);
            SocketChannel socketChannel = serverSocketChannel.accept();
            int messageLength = 8;//accept 8 byte from client
            while (true){
                int byteRead = 0;

                while (byteRead < messageLength){
                    long read = socketChannel.read(byteBuffers);
                    byteRead += read;
                    System.out.println("byteRead: " + byteRead);
                    Arrays.stream(byteBuffers)
                            .map(byteBuffer -> "position: " + byteBuffer.position() + " ,limit: " + byteBuffer.limit())
                            .forEach(System.out::println);
                }

                Arrays.stream(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

                int byteWrite = 0;
                while (byteWrite < messageLength){
                    long write = socketChannel.write(byteBuffers);
                    byteWrite += write;
                }

                Arrays.stream(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
                System.out.println("byteRead: " + byteRead + " ,byteWrite: " + byteWrite + " ,messageLength: " + messageLength);
            }

        }


    }

}
