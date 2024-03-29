package common;

import common.protocal.AuthRequestProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class ImClient {


    private SocketChannel socketChannel;

    private Bootstrap client;

    private NioEventLoopGroup eventExecutors;

    public void connect(String host, int port) throws InterruptedException {
        eventExecutors = new NioEventLoopGroup();
        client = new Bootstrap();
        client.group(eventExecutors);
        client.channel(NioSocketChannel.class);
        client.option(ChannelOption.TCP_NODELAY, true);
        client.option(ChannelOption.SO_KEEPALIVE, true);
        client.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ByteBuf delimit = Unpooled.copiedBuffer("$_".getBytes());
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(4096, delimit));
//                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new ImClientHandler());
            }
        });

        System.out.println("完成Netty客户端的配置");
        // 发起连接
        ChannelFuture channelFuture = client.connect(host, port).sync();
        System.out.println("完成对TCP接入系统的连接...");

        // 给连接加上监听器
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                socketChannel = (SocketChannel) channelFuture1.channel();
                System.out.println("跟TCP接入系统完成长连接的建立");
            }else {
                channelFuture1.channel().close();
                eventExecutors.shutdownGracefully();
            }
        });
    }

    public void auth(String userId,String token){
        AuthRequestProto.AuthRequest body = AuthRequestProto.AuthRequest.newBuilder()
                .setUid(userId)
                .setToken(token)
                .setTimestamp(System.currentTimeMillis())
                .build();

        Request request = new Request(
                Constants.APP_SDK_VERSION_1,
                Constants.REQUEST_TYPE_AUTH,
                Constants.SEQUENCE_DEFAULT,
                body.toByteArray());

        socketChannel.writeAndFlush(request.getByteBuf());
        System.out.println("向TCP接入系统发起用户认证请求");
    }

    /**
     * 发送消息
     */
    public void send(String userId,String msg) {
        byte[] messageBytes = (msg + "|" + userId+"$_").getBytes();
        ByteBuf messageBuf = Unpooled.buffer(messageBytes.length);
        messageBuf.writeBytes(messageBytes);
        socketChannel.writeAndFlush(messageBuf);
        System.out.println("向TCP接入系统发送第一条消息,");
    }

    public void close(String msg) {
        socketChannel.close();
        eventExecutors.shutdownGracefully();
    }
}
