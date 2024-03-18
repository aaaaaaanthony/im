package demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Response {

    public static final int HEADER_LENGTH = 20;
    public static final byte[] DELIMIT = "$_".getBytes();


    private int headerLength;
    private int sequence;
    private int requestType;
    private int appSdkVersion;
    private int bodyLength;
    private byte[] body;

    private ByteBuf buffer;



    // 封装响应
    public Response(Request request, byte[] body) {
        this.headerLength = request.getHeaderLength();
        this.sequence = request.getSequence();
        this.requestType = request.getRequestType();
        this.appSdkVersion = request.getAppSdkVersion();
        this.bodyLength = body.length;
        this.body = body;

        buffer = Unpooled.buffer(HEADER_LENGTH+body.length+DELIMIT.length);
        buffer.writeInt(headerLength);
        buffer.writeInt(appSdkVersion);
        buffer.writeInt(requestType);
        buffer.writeInt(sequence);
        buffer.writeInt(body.length);
        buffer.writeBytes(body);
        buffer.writeBytes(DELIMIT);
    }

    // 解析响应
    public Response(ByteBuf byteBuf) {
        this.headerLength = byteBuf.readInt();
        this.appSdkVersion = byteBuf.readInt();
        this.requestType = byteBuf.readInt();
        this.sequence = byteBuf.readInt();
        this.bodyLength = byteBuf.readInt();
        this.body=new byte[buffer.readableBytes()];
        byteBuf.readBytes(body);
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
