package dispatcher;

import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * im-gateway-tcp 网络连接管理组件
 */
public class GatewayInstanceManager {

    private GatewayInstanceManager(){
    }

    static class Singleton {
        private static GatewayInstanceManager instance = new GatewayInstanceManager();

    }

    public static GatewayInstanceManager getInstance() {
        return Singleton.instance;
    }

    private ConcurrentHashMap<String, SocketChannel> gatewayInstance = new ConcurrentHashMap<>();


    public void addGatewayInstance(String channelId, SocketChannel socketChannel) {
        gatewayInstance.put(channelId, socketChannel);
    }

    public void removeGatewayInstance(String channelId) {
        gatewayInstance.remove(channelId);
    }
}
