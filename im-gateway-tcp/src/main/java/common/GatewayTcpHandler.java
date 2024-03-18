package common;

import common.protocal.AuthRequestProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

public class GatewayTcpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("跟客户端完成连接:"+ctx.channel().remoteAddress().toString());

    }

    /**
     * 客户端端口了
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        SessionManager instance = SessionManager.getInstance();
        instance.removeSession(channel);
        System.out.println("断开连接:" + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取请求处理组件
        ByteBuf requestBuffer = (ByteBuf) msg;
        Request request = new Request(requestBuffer);

        RequestHandler requestHandler = RequestHandler.getInstance();

        // 如果是认证请求
        if (request.getRequestType() == Constants.REQUEST_TYPE_AUTH) {
            AuthRequestProto.AuthRequest authRequest = AuthRequestProto.AuthRequest.parseFrom(request.getBody());
            // 调用业务逻辑组件进行认证
            requestHandler.auth(authRequest);
            // 设置本地Session,维护uid和session的映射关系
            // 维护channelId和uid的关系
            SessionManager sessionManager = SessionManager.getInstance();
            sessionManager.addSession(authRequest.getUid(), (SocketChannel) ctx.channel());

        }
        System.out.println("服务端收到消息===>" + request.getBody().length);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
