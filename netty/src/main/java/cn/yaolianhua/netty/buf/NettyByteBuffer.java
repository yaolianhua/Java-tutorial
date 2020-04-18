package cn.yaolianhua.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.stream.IntStream;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-24 08:52
 **/
public class NettyByteBuffer {
    public static void main(String[] args) {
        bytebuf1();
        System.out.println("-----------------------");
        byteBuf2();
    }


    //在netty 的buffer中，不需要使用flip 进行反转,底层维护了 readerIndex 和 writerIndex
    private static void bytebuf1(){
        ByteBuf buffer = Unpooled.buffer(5);
        IntStream.range(0,5).forEach(buffer::writeByte);
        System.out.println("capacity= " + buffer.capacity());

        System.out.println(" -- buffer.getByte(index) -- ");
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.print(buffer.getByte(i) + " ");
        }
        System.out.println("ByteBuf= " + buffer);//UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 5, cap: 5)
        System.out.println(" -- buffer.readByte() -- ");
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.print(buffer.readByte() + " ");
        }
        System.out.println("ByteBuf= " + buffer);//UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 5, widx: 5, cap: 5)
    }

    private static void byteBuf2(){
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", CharsetUtil.UTF_8);
        if(byteBuf.hasArray()) { // true
            byte[] content = byteBuf.array();
            //将 content 转成字符串
            System.out.println(new String(content, CharsetUtil.UTF_8).trim());

            System.out.println("byteBuf= " + byteBuf);

            System.out.println(byteBuf.arrayOffset()); // 0
            System.out.println(byteBuf.readerIndex()); // 0
            System.out.println(byteBuf.writerIndex()); // 12
            System.out.println(byteBuf.capacity()); // 36

            //System.out.println(byteBuf.readByte()); //
            System.out.println(byteBuf.getByte(0)); // 104

            int len = byteBuf.readableBytes(); //可读的字节数  12
            System.out.println("len=" + len);

            //使用for取出各个字节
            for(int i = 0; i < len; i++) {
                System.out.print((char) byteBuf.getByte(i) + " ");
            }

            //按照某个范围读取
            System.out.print(byteBuf.getCharSequence(0, 4, CharsetUtil.UTF_8) + " ");
            System.out.println(byteBuf.getCharSequence(4, 9, CharsetUtil.UTF_8) + " ");


        }
    }


}
