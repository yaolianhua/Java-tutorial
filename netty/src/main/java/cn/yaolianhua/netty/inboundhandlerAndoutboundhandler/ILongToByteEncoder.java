package cn.yaolianhua.netty.inboundhandlerAndoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-25 16:12
 **/
public class ILongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("ILongToByteEncoder encode() invoked");
        out.writeLong(msg);
    }
}
