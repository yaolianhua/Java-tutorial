package cn.yaolianhua.netty.rpc.consumer;

import cn.yaolianhua.netty.rpc.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-27 10:35
 **/
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        NettyClient consumer = new NettyClient();

        for (;;){
            TimeUnit.SECONDS.sleep(5);
            HelloService proxy = (HelloService) consumer.getProxy(HelloService.class);
            String hello = proxy.sayHello("Hello rpc server");
            System.out.println(hello);
        }
    }

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private  static NettyClientHandler clientHandler;
    private static void startClient(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        clientHandler = new NettyClientHandler();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new StringEncoder())
                                    .addLast(new StringDecoder())
                                    .addLast(clientHandler);
                        }
                    });
            bootstrap.connect("127.0.0.1", 7000).sync();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public  Object getProxy(final Class<?> clazz){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{clazz},((proxy, method, args) -> {
            if (clientHandler == null)
                startClient();
            clientHandler.setParam("rpcProtocol0#" + args[0]);
            return executorService.submit(clientHandler).get();
            })
        );
    }
}

class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable{
    private  ChannelHandlerContext context;
    private String response;
    private String param;

    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        System.out.println("call() current thread: " + Thread.currentThread().getName());
        wait();
        return response;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response = msg.toString();
        System.out.println("channelRead() current thread: " + Thread.currentThread().getName());
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }

    public void setParam(String param) {
        this.param = param;
    }
}
