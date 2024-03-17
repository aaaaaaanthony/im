package demo;

import io.netty.buffer.ByteBuf;

public class request {

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

    public request(ByteBuf requestByteBuf) {
        this.headerLength = requestByteBuf.readInt();
        this.appSdkVersion = requestByteBuf.readInt();
        this.requestType = requestByteBuf.readInt();
        this.sequence = requestByteBuf.readInt();
        this.bodyLength = requestByteBuf.readInt();
        this.body= requestByteBuf.readBytes()
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
}
