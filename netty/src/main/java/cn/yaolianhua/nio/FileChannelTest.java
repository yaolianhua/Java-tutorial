package cn.yaolianhua.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-04 12:00
 **/
public class FileChannelTest {
    private static final String CURRENT_PROJECT_PATH = System.getProperty("user.dir") + "/netty/";

    public static void main(String[] args) throws IOException {
            writeStringToFile();
//        readStringFromFile();
//        readWriteFromFile();
//        transferFrom();
//        mappedByteBuffer();
    }

    private static void writeStringToFile() throws IOException {
        String data = "你好 NIO";
        try (FileOutputStream os =
                     new FileOutputStream(CURRENT_PROJECT_PATH + "writeStringToFile.txt")) {
            FileChannel osChannel = os.getChannel();//get channel
            ByteBuffer byteBuffer = ByteBuffer.allocate(256);
            byteBuffer.put(data.getBytes());//write data to buffer
            byteBuffer.flip();//flip
            osChannel.write(byteBuffer);//write data to channel from buffer
        }
    }

    private static void readStringFromFile() throws IOException {
        File file = new File(CURRENT_PROJECT_PATH + "writeStringToFile.txt");
        FileChannel isChannel;
        try (FileInputStream is = new FileInputStream(file)) {
            isChannel = is.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(((int) file.length()));
            isChannel.read(byteBuffer);
            System.out.println(new String(byteBuffer.array()));
        }

    }

    private static void readWriteFromFile() throws IOException{

        try (FileInputStream is = new FileInputStream(CURRENT_PROJECT_PATH + "readWriteFromFile.txt")) {
            try (FileOutputStream  os = new FileOutputStream(CURRENT_PROJECT_PATH + "readWriteFromFile.copy.txt")) {
                FileChannel isChannel = is.getChannel();
                FileChannel osChannel = os.getChannel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while (true){
                    /**
                     * public final Buffer clear() {
                     *         position = 0;
                     *         limit = capacity;
                     *         mark = -1;
                     *         return this;
                     * }
                     */
                    byteBuffer.clear();
                    int read = isChannel.read(byteBuffer);
                    System.out.println("read: " + read);
                    if (read == -1)
                        break;
                    byteBuffer.flip();
                    osChannel.write(byteBuffer);
                }
            }
        }
    }

    private static void transferFrom() throws IOException{
        try (FileInputStream is =
                     new FileInputStream(FileChannelTest.class.getResource("").getPath() + "FileChannelTest.class")) {
            try (FileOutputStream os = new FileOutputStream(CURRENT_PROJECT_PATH + "FileChannelTest.class")) {
                FileChannel isChannel = is.getChannel();
                FileChannel osChannel = os.getChannel();
                osChannel.transferFrom(isChannel,0,isChannel.size());
            }
        }
    }

    private static void mappedByteBuffer() throws IOException{
        try (RandomAccessFile accessFile = new RandomAccessFile(CURRENT_PROJECT_PATH + "writeStringToFile.txt", "rw")) {
            FileChannel accessFileChannel = accessFile.getChannel();
            MappedByteBuffer mappedByteBuffer =
                    accessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, accessFileChannel.size());
            mappedByteBuffer.put("呵呵".getBytes(),0,"呵呵".getBytes().length);

        }
    }

}
