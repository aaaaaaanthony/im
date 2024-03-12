package demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ImClient {

    private SocketChannel socketChannel;

    private Bootstrap client;

    private NioEventLoopGroup eventExecutors;

    public void connect(String host, String port) {
        eventExecutors = new NioEventLoopGroup();
        try {
            this.client = new Bootstrap();
            client.group(eventExecutors);
            client.channel(NioServerSocketChannel.class);
            client.option(ChannelOption.TCP_NODELAY, true);
            client.option(ChannelOption.SO_KEEPALIVE, true);
            client.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ImClientHandler());
                }
            });
            // 发起连接
            ChannelFuture channelFuture = client.connect(host, Integer.parseInt(port)).sync();
            // 给连接加上监听器
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        socketChannel = (SocketChannel) channelFuture.channel();
                    }else {
                        channelFuture.channel().close();
                        eventExecutors.shutdownGracefully();
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public void send(String msg) {

    }

    public void close(String msg) {
        socketChannel.close();
        eventExecutors.shutdownGracefully();
    }
}
