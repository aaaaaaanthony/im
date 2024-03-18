package common;

import io.netty.buffer.ByteBuf;

public class Request extends Message {

    public Request(int appSdkVersion,int requestType,int sequence,byte[] body) {
        super(appSdkVersion,Constants.MESSAGE_TYPE_REQUEST,requestType,sequence,body);
    }

    public Request(ByteBuf byteBuf) {
        super(byteBuf);
    }

}
