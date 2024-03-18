package common.dispatcher;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class DispatcherInstanceManager {

    private DispatcherInstanceManager() {}
    static class Singleton{ static DispatcherInstanceManager instance = new DispatcherInstanceManager();}
    private static List<DispatcherInstanceAddress> dispatcherInstanceAddresses = new ArrayList<>();

    static {dispatcherInstanceAddresses.add(new DispatcherInstanceAddress("localhost", "127.0.0.1", 8090));}


    /**
     * 随机选择一个分发实例
     */
    public DispatcherInstance chooseDispatcherInstance() {
        Random random = new Random();
        int i = random.nextInt(dispatcherInstances.size());
        return dispatcherInstances.get(i);
    }

    public static DispatcherInstanceManager getInstance() {
        return Singleton.instance;
    }

    private List<DispatcherInstance> dispatcherInstances = new CopyOnWriteArrayList<>();

    public void init() throws InterruptedException {
        for (DispatcherInstanceAddress dispatcherInstanceAddress : dispatcherInstanceAddresses) {
            connectDispatcherInstance(dispatcherInstanceAddress);
        }
    }

    private void connectDispatcherInstance(DispatcherInstanceAddress dispatcherInstanceAddress) throws InterruptedException {

        final NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        client.group(eventExecutors);
        client.channel(NioSocketChannel.class);
        client.option(ChannelOption.TCP_NODELAY, true);
        client.option(ChannelOption.SO_KEEPALIVE, true);
        client.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ByteBuf delimit = Unpooled.copiedBuffer("$_".getBytes());
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimit));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new DispatcherInstanceHandler());
            }
        });

        // 发起连接
        ChannelFuture channelFuture = client.connect(dispatcherInstanceAddress.getIp(), dispatcherInstanceAddress.getPort()).sync();

        // 给连接加上监听器
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                SocketChannel socketChannel = (SocketChannel) channelFuture1.channel();
                DispatcherInstance dispatcherInstance = new DispatcherInstance(socketChannel);
                dispatcherInstances.add(dispatcherInstance);

            }else {
                channelFuture1.channel().close();
                eventExecutors.shutdownGracefully();
            }
        });
    }


}
