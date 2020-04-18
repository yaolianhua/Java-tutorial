package cn.yaolianhua.netty.prorocoltcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-25 21:28
 **/
public class Server {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer());
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
class ServerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                .addLast(new MessageDecoder())
                .addLast(new MessageEncoder())
                .addLast(new ServerHandler());
    }
}

class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        String message = new String(msg.getContent(), CharsetUtil.UTF_8);
        System.out.println("message len: " + msg.getLen());
        System.out.println("Server received message: " + message);
        System.out.println("count: " + (++this.count));
        MessageProtocol protocol = new MessageProtocol(UUID.randomUUID().toString().getBytes(Charset.defaultCharset()).length,
                UUID.randomUUID().toString().getBytes(Charset.defaultCharset()));
        ctx.writeAndFlush(protocol);
    }
}