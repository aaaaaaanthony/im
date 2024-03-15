package demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.SocketChannel;


/**
 * 消息推送管理最贱
 */
public class PushManager {

    public void start(){
        new PushThread().start();
    }

    class PushThread extends Thread{
        @Override
        public void run() {
            while (true) {
                try {

                    Thread.sleep(60 *1000 );

                    String userId = "anthony";

                    NettyChannelManager nettyChannelManager = NettyChannelManager.getInstance();
                    SocketChannel channels = nettyChannelManager.getChannels(userId);

                    if (channels != null) {
                        byte[] responseBuf = "未认证用户,不能处理请求".getBytes();
                        ByteBuf buffer = Unpooled.buffer(responseBuf.length);
                        buffer.writeBytes(responseBuf);
                        channels.writeAndFlush(buffer);
                    }else {
                        System.out.println("目标用户已经下线");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
