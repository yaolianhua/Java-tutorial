package cn.yaolianhua.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.time.LocalDateTime;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-24 17:18
 **/
public class WebSocketServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    ////因为基于http协议，使用http的编码和解码器
                                    .addLast(new HttpServerCodec())
                                    //是以块方式写，添加ChunkedWriteHandler处理器
                                    .addLast(new ChunkedWriteHandler())
                                    //1. http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
                                    //2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
                                    .addLast(new HttpObjectAggregator(4096))
                                    /*
                                    1. 对应websocket ，它的数据是以 帧(frame) 形式传递
                                    2. 可以看到WebSocketFrame 下面有六个子类
                                    3. 浏览器请求时 ws://localhost:7000/ws 表示请求的uri
                                    4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
                                    5. 是通过一个 状态码 101
                                     */
                                    .addLast(new WebSocketServerProtocolHandler("/ws"))
                                    .addLast(new TextWebSocketFrameHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            System.out.println("--WebSocket Server Start--");
            channelFuture.channel().closeFuture().sync();


        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("Server received: " + msg.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame(LocalDateTime.now() + " " + msg.text()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler added: " + ctx.channel().id().asLongText() + " " + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler removed: " + ctx.channel().id().asLongText() + " " + ctx.channel().id().asShortText());
    }
}
