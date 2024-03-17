package demo;

import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {

    private ClientManager() {

    }

    public boolean isClientConnected(String userId) {
        return clients.containsKey(userId);
    }

    static class Singleton{
        private static ClientManager instance = new ClientManager();
    }

    public static ClientManager getInstance() {
        return Singleton.instance;
    }

    private ConcurrentHashMap<String, SocketChannel> clients = new ConcurrentHashMap<>();
    /**
     * 客户端ID
     */
    private ConcurrentHashMap<String, String> channelId2uid = new ConcurrentHashMap<>();

    public void addClient(String userId, SocketChannel socketChannel) {
        channelId2uid.put(socketChannel.id().asLongText(), userId);
        clients.put(userId, socketChannel);
    }

    public SocketChannel getClient(String userId) {
        return clients.get(userId);
    }

    public void removeChannel(SocketChannel socketChannel) {
        String userId = channelId2uid.get(socketChannel.id().asLongText());
        channelId2uid.remove(socketChannel.id().asLongText());
        clients.remove(userId);

    }

}
