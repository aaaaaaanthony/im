package demo.dispatcher;

import demo.Constants;
import demo.Request;
import demo.protocal.AuthRequestProto;
import demo.protocal.AuthResponseProto;
import io.netty.channel.socket.SocketChannel;

/**
 * 分发系统的实例
 * @author anthony
 */
public class DispatcherInstance {
    private SocketChannel socketChannel;

    public DispatcherInstance(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void auth(AuthRequestProto.AuthRequest authRequest) {
        Request request = new Request(Constants.APP_SDK_VERSION_1,
                Constants.REQUEST_TYPE_AUTH,
                Constants.SEQUENCE_DEFAULT,
                authRequest.toByteArray());
        socketChannel.writeAndFlush(request.getByteBuf());
    }
}
