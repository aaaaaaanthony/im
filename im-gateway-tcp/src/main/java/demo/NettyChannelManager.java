package demo;

import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelManager {

    private NettyChannelManager() {

    }

    public boolean exist(String userId) {
        return channels.contains(userId);
    }

    static class Singleton{
        private static NettyChannelManager instance = new NettyChannelManager();
    }

    public static NettyChannelManager getInstance() {
        return Singleton.instance;
    }

    private ConcurrentHashMap<String, SocketChannel> channels = new ConcurrentHashMap<>();
    /**
     * 客户端ID
     */
    private ConcurrentHashMap<String, String> channelIds = new ConcurrentHashMap<>();

    public void addChannel(String userId, SocketChannel socketChannel) {
        channelIds.put(socketChannel.remoteAddress().getHostName(), userId);
        channels.put(userId, socketChannel);
    }

    public SocketChannel getChannels(String userId) {
        return channels.get(userId);
    }

    public void removeChannel(SocketChannel socketChannel) {
        String userId = channelIds.get(socketChannel.remoteAddress().getHostName());
        channelIds.remove(socketChannel.remoteAddress().getHostName());
        channels.remove(userId);

    }

}
