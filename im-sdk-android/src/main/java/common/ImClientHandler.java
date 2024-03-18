package common;

import common.protocal.AuthResponseProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ImClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = new Response((ByteBuf) msg);
        if (response.getRequestType()== Constants.REQUEST_TYPE_AUTH){
            AuthResponseProto.AuthResponse authResponseProto = AuthResponseProto.AuthResponse.parseFrom(response.getBody());
            System.out.println("认证请求:"+authResponseProto.toString());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

    }
}
