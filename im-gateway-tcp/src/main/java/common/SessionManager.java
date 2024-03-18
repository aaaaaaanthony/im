package common;

import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private SessionManager() {

    }

    public boolean isClientConnected(String userId) {
        return sessions.containsKey(userId);
    }

    static class Singleton{
        private static SessionManager instance = new SessionManager();
    }

    public static SessionManager getInstance() {
        return Singleton.instance;
    }

    /**
     * 存储uid到客户端的映射
     */
    private ConcurrentHashMap<String, SocketChannel> sessions = new ConcurrentHashMap<>();
    /**
     * 客户端ID,
     * changnelId 2 uid
     */
    private ConcurrentHashMap<String, String> channelId2uid = new ConcurrentHashMap<>();

    public void addSession(String userId, SocketChannel socketChannel) {
        String hostName = socketChannel.remoteAddress().getHostName();
        channelId2uid.put(hostName, userId);
        sessions.put(userId, socketChannel);
    }

    public SocketChannel getSession(String userId) {
        return sessions.get(userId);
    }

    public void removeSession(SocketChannel socketChannel) {
        String hostName = socketChannel.remoteAddress().getHostName();
        channelId2uid.remove(hostName);
        sessions.remove(hostName);

    }

}
