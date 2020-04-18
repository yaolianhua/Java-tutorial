package cn.yaolianhua.netty.prorocoltcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.stream.IntStream;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-25 21:29
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

class ClientInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                .addLast(new MessageEncoder())
                .addLast(new MessageDecoder())
                .addLast(new ClientHandler());
    }
}
class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        IntStream.range(0,5).forEach(i -> {
            MessageProtocol messageProtocol =
                    new MessageProtocol("你好,netty！".getBytes(Charset.defaultCharset()).length,
                            "你好,netty！".getBytes(Charset.defaultCharset()));
            ctx.writeAndFlush(messageProtocol);
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        String message = new String(msg.getContent(), CharsetUtil.UTF_8);
        System.out.println("message len: " + msg.getLen());
        System.out.println("Client Received message from server: " + message + " ");
        System.out.println("count: " + (++this.count));

    }


}
