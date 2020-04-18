package cn.yaolianhua.nio;

import java.nio.IntBuffer;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-04 11:27
 **/
public class BasicBufferTest {

    public static void main(String[] args) {

        intBuffer();
    }

    private static void intBuffer(){
        IntBuffer intBuffer = IntBuffer.allocate(3);
        int[] ints = new int[intBuffer.capacity()];
        for (int i = 0; i < intBuffer.capacity(); i++) {
           ints[i] = (i + 1) * 10;
        }
        intBuffer.put(ints);
        intBuffer.flip();//读写切换
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }


}
