package cn.yaolianhua.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-05 16:50
 **/
public class NIOClient {
    public static void main(String[] args) throws IOException {
        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 7000))) {
            long start = System.currentTimeMillis();
            String file = System.getProperty("user.dir") + "/netty/jetbrains-agent-latest.zip";
            try (FileChannel fileChannel = new FileInputStream(file).getChannel()) {
                long transferTo = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
                System.out.println("send total byte: " + transferTo + " spend time: " + (System.currentTimeMillis() - start) + "ms");
            }
        }

    }
}
