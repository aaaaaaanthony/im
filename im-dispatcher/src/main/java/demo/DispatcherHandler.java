package demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

public class    DispatcherHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        GatewayInstanceManager instance = GatewayInstanceManager.getInstance();
        instance.addGatewayInstance(channel.id().asLongText(), channel);
        System.out.println("跟客户端完成连接:"+ctx.channel().remoteAddress().toString());
    }

    /**
     * 客户端端口了
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        GatewayInstanceManager instance = GatewayInstanceManager.getInstance();
        instance.removeGatewayInstance(channel.id().asLongText());
        System.out.println("断开连接:" + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
