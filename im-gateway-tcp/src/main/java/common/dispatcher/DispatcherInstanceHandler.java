package common.dispatcher;

import common.Constants;
import common.Response;
import common.SessionManager;
import common.protocal.AuthResponseProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

/**
 * 做为 分发系统的实例 的客户端
 */
public class DispatcherInstanceHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Response response = new Response((ByteBuf) msg);
        if (response.getRequestType() == Constants.REQUEST_TYPE_AUTH) {

            AuthResponseProto.AuthResponse authResponse = AuthResponseProto.AuthResponse.parseFrom(response.getBody());
            // 调用业务逻辑组件进行认证
            String uid = authResponse.getUid();
            if (authResponse.getStatus()==Constants.RESPONSE_STATUS_OK) {
                // 这里应该吧session信息放入redis的
                // key=uid  value={}
                SessionManager sessionManager = SessionManager.getInstance();
                SocketChannel session = sessionManager.getSession(uid);
                session.writeAndFlush(response);

            }


        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
