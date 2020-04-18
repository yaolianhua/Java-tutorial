package cn.yaolianhua.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-03 16:41
 **/
public class BIOServer {

    public static void main(String[] args) throws IOException {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(7777);
        while (true){
            Socket socket = serverSocket.accept();
            threadPool.execute(()->clientHandle(socket));
        }

    }

    private static void clientHandle(final Socket socket) {
        try(InputStream inputStream = socket.getInputStream()) {
            byte[] bytes = new byte[1024];
            for (; ; ) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read, Charset.defaultCharset()));
                } else break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
