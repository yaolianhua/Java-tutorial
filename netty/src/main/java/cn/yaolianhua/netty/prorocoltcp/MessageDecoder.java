package cn.yaolianhua.netty.prorocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-25 21:31
 **/
public class MessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MessageDecoder decode() invoked");
        int len = in.readInt();
        byte[] content = new byte[len];
        in.readBytes(content);
        //transfer to next handler
        out.add(new MessageProtocol(len,content));
    }
}
