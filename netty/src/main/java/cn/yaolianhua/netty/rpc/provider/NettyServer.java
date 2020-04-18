package cn.yaolianhua.netty.rpc.provider;

import cn.yaolianhua.netty.rpc.HelloService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.yaolianhua.netty.rpc.provider.NettyServer.getBean;
import static java.lang.Thread.currentThread;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-27 10:35
 **/
public class NettyServer {
    public static void main(String[] args) {
        startServer0("127.0.0.1",7000);
    }

    private static Map<Class,Object> map = new HashMap<>();
    static {
        map.put(HelloService.class,new HelloServiceImpl());
    }
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz){
        return (T) map.get(clazz);
    }

    public static void startServer0(String host,int port){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
            System.out.println("--Netty Server Start--");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static  AtomicInteger count = new AtomicInteger(0);
    private final String protocol = "rpcProtocol0#";
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //protocol: rpcProtocol0#string
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String m = msg.toString();
        System.out.println(m);
        if (m.startsWith(protocol)){
            String message = m.substring(protocol.length());
            HelloService helloService = getBean(HelloService.class);
            System.out.println("HelloService hashcode: " + helloService.hashCode());
            ctx.writeAndFlush(helloService.sayHello(message));
            System.out.println("count: " + count.incrementAndGet());
        }
    }
}
