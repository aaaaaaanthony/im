package demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
        NettyChannelManager instance = NettyChannelManager.getInstance();
        instance.removeChannel(channel);
        System.out.println("断开连接:" + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyChannelManager nettyChannelManager = NettyChannelManager.getInstance();


        ByteBuf messageByteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[messageByteBuf.readableBytes()];
        messageByteBuf.readBytes(bytes);
        String message = new String(bytes);
        System.out.println("服务端收到消息===>"+message);

        if (message.startsWith("发起用户认证")) {
            String token = message.split("|")[2];

            // 拿到这个token,进行校验,这里就不写逻辑了

            // 如果认证成功的话,就可以把这个连接缓存起来了
            String userId = message.split("|")[1];
            nettyChannelManager.addChannel(userId, (SocketChannel) ctx.channel());
            System.out.println("对用户发起的认证确认完毕,缓存客户端长连接:" + ctx.channel().remoteAddress().toString());

        }else {
            String userId = message.split("|")[1];
            if (!nettyChannelManager.exist(userId)) {
                System.out.println("未认证用户,不能处理请求");
                byte[] responseBuf = "未认证用户,不能处理请求".getBytes();
                ByteBuf buffer = Unpooled.buffer(responseBuf.length);
                buffer.writeBytes(responseBuf);
                ctx.writeAndFlush(buffer);
            }else {
                System.out.println("将消息分发出去到KafKa:" + message);
            }

        }


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
