package cn.yaolianhua.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.stream.IntStream;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-23 10:37
 **/
public class Client {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer());
        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class ClientInitializer extends ChannelInitializer{
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new ClientHandler());
    }
}
class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    private int count;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        IntStream.range(0,10).forEach(i -> {
            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello Server,message i = " + i + " ", CharsetUtil.UTF_8);
            ctx.writeAndFlush(byteBuf);
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String message = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("Client Received message from server: " + message + " ");
        System.out.println("count: " + (++this.count));

    }


}
