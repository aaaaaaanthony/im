package common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Message {

    /**
     * 消息头长度
     */
    protected int headerLength;
    /**
     * 客户端版本号
     */
    protected int appSdkVersion;

    /**
     * 消息类型,请求还是响应
     */
    protected int messageType;
    /**
     * 请求类型
     */
    protected int requestType;
    /**
     * 请求顺序
     */
    protected int sequence;
    /**
     * 消息体长度
     */
    protected int bodyLength;
    /**
     * 消息体
     */
    protected byte[] body;
    /**
     * 封装完的完整消息
     */
    protected ByteBuf buffer;

    public Message() {
    }

    /**
     * 封装消息
     * @param appSdkVersion
     * @param requestType
     * @param sequence
     * @param body
     */
    public Message(int appSdkVersion,int messageType,int requestType,int sequence,byte[] body) {
        this.headerLength=Constants.HEADER_LENGTH;
        this.appSdkVersion = appSdkVersion;
        this.messageType = messageType;
        this.requestType = requestType;
        this.sequence = sequence;
        this.bodyLength = body.length;
        this.body = body;

        // 封装了完整的请求消息
        buffer = Unpooled.buffer(Constants.HEADER_LENGTH+body.length+Constants.DELIMIT.length);
        buffer.writeInt(Constants.HEADER_LENGTH);
        buffer.writeInt(appSdkVersion);
        buffer.writeInt(messageType);
        buffer.writeInt(requestType);
        buffer.writeInt(sequence);
        buffer.writeInt(body.length);
        buffer.writeBytes(body);
        buffer.writeBytes(Constants.DELIMIT);
    }

    /**
     * 解析消息
     * @param byteBuf
     */
    public Message(ByteBuf byteBuf) {
        this.headerLength = byteBuf.readInt();
        this.appSdkVersion = byteBuf.readInt();
        this.messageType = byteBuf.readInt();
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

    public ByteBuf getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
