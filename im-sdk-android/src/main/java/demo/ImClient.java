package demo;

import demo.protocal.AuthRequestProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class ImClient {

    // 消息头的长度
    private static final int request_header_length = 20;
    // app sdk版本号
    private static final int app_sdk_version = 1;
    // 请求类型:用户认证
    private static final int request_type_auth = 1;
    // 消息顺序
    private static final int sequene_default = 1;
    private static final byte[] delimit = "$_".getBytes();

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
        byte[] messageBytes = ("发起用户认证|"+userId+"|"+token+"$_").getBytes();


        AuthRequestProto.AuthRequest body = AuthRequestProto.AuthRequest.newBuilder()
                .setUid(userId)
                .setToken(token)
                .setTimestamp(System.currentTimeMillis())
                .build();
        byte[] byteArray = body.toByteArray();

        ByteBuf messageBuf = Unpooled.buffer(request_header_length+byteArray.length+delimit.length);
        messageBuf.writeInt(request_header_length);
        messageBuf.writeInt(app_sdk_version);
        messageBuf.writeInt(request_type_auth);
        messageBuf.writeInt(sequene_default);
        messageBuf.writeInt(byteArray.length);
        messageBuf.writeBytes(byteArray);
        messageBuf.writeBytes(delimit);
        socketChannel.writeAndFlush(messageBuf);
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
