package common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static common.Constants.DELIMIT;
import static common.Constants.HEADER_LENGTH;

public class Response extends Message {

    // 封装响应
    public Response(Request request, byte[] body) {
        super(request.getAppSdkVersion(),
                Constants.MESSAGE_TYPE_RESPONSE,
                request.getRequestType(),
                request.getSequence(),
                body
        );
    }

    // 解析响应
    public Response(ByteBuf buffer) {
        super(buffer);
    }
}
