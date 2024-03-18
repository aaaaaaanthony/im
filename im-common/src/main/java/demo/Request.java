package demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Request {

    // 消息头
    private int headerLength;

    // sdk版本号
    private int appSdkVersion;

    // 请求类型
    private int requestType;

    // 顺序
    private int sequence;

    // 消息体长度
    private int bodyLength;

    private byte[] body;

    private ByteBuf byteBuf;


    public Request(int appSdkVersion,int requestType,int sequence,byte[] body) {
        this.headerLength=Constants.HEADER_LENGTH;
        this.appSdkVersion = appSdkVersion;
        this.requestType = requestType;
        this.sequence = sequence;
        this.bodyLength = body.length;
        this.body = body;

        // 封装了完整的请求消息
        byteBuf = Unpooled.buffer(Constants.HEADER_LENGTH+body.length+Constants.DELIMIT.length);
        byteBuf.writeInt(Constants.HEADER_LENGTH);
        byteBuf.writeInt(Constants.APP_SDK_VERSION_1);
        byteBuf.writeInt(requestType);
        byteBuf.writeInt(sequence);
        byteBuf.writeInt(body.length);
        byteBuf.writeBytes(body);
        byteBuf.writeBytes(Constants.DELIMIT);
    }

    public Request(ByteBuf byteBuf) {
        this.headerLength = byteBuf.readInt();
        this.appSdkVersion = byteBuf.readInt();
        this.requestType = byteBuf.readInt();
        this.sequence = byteBuf.readInt();
        this.bodyLength = byteBuf.readInt();
        this.body = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(body);
    }



    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public int getAppSdkVersion() {
        return appSdkVersion;
    }

    public void setAppSdkVersion(int appSdkVersion) {
        this.appSdkVersion = appSdkVersion;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }
}
