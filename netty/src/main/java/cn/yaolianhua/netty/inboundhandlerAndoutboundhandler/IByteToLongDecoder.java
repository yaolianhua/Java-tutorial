package cn.yaolianhua.netty.inboundhandlerAndoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-25 16:11
 **/
public class IByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("IBytesToLongDecoder decode() invoked");
        if (in.readableBytes() >= 8){//long 8字节
            out.add(in.readLong());
        }
    }
}
