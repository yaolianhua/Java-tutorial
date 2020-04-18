package cn.yaolianhua.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-02-09 14:43
 **/
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        //NettyRuntime.availableProcessors() * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128)//线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置活动为连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MyNettyServerHandler());//给管道设置处理器
                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器
            System.out.println("server is ready");
            //start server
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class MyNettyServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server read thread: " + Thread.currentThread().getName());
        ChannelPipeline pipeline = ctx.pipeline();////本质是一个双向链表, 出站入站
        Channel channel = ctx.channel();
        System.out.println("client msg: " + ((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        System.out.println("client address: " + channel.remoteAddress());

        //异步执行耗时长的业务
        //解决方案一 自定义普通任务 -> 提交该channel对应NIOEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
                ctx.writeAndFlush(Unpooled.copiedBuffer("（taskQueue）sleep 5s to say \'Hello Client\'",CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        ctx.channel().eventLoop().execute(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
                ctx.writeAndFlush(Unpooled.copiedBuffer("（taskQueue）sleep 8s to say \'Hello Client\'",CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        //解决方案二 自定义定时任务 -> 该任务是提交到 scheduleTaskQueue中
        // ?? scheduleTaskQueue 也是在taskQueue中等待？延迟时间会在以上线程阻塞8s后同步消耗掉
        ctx.channel().eventLoop().schedule(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                ctx.writeAndFlush(Unpooled.copiedBuffer("（scheduleTaskQueue）sleep 9s to say \'Hello Client\'",CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        },8,TimeUnit.SECONDS);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Client",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
