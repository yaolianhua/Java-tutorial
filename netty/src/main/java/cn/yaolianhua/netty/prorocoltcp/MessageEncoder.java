package cn.yaolianhua.netty.prorocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-25 21:31
 **/
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MessageEncoder encode() invoked");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
