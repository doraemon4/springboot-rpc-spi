package com.stephen.learning.blade.core.sender;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 15:04
 * @Version: 1.0
 */
public class NettyClient extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;

    private String response;

    private final Object obj = new Object();

    public NettyClient(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, String arg1) throws Exception {
        this.response = arg1;

        synchronized (obj) {
            obj.notifyAll(); // 收到响应，唤醒线程
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception", cause);
        ctx.close();
    }

    public String send(String request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline()
                            .addLast(new StringDecoder(CharsetUtil.UTF_8)) // 将 RPC 响应进行解码（为了处理响应）
                            .addLast(new StringEncoder(CharsetUtil.UTF_8)) // 将RPC请求进行编码（为了发送请求）
                            .addLast(NettyClient.this); // 使用 RpcClient 发送 RPC 请求
                }
            });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync();

            synchronized (obj) {
                obj.wait(); // 未收到响应，使线程等待
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }
            System.out.println("返回结果==>" + response);
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient("localhost", 8091);
        String rlt = client.send("123\n\r");
        System.out.println("====>" + rlt);
    }

}