package common;


import com.google.protobuf.InvalidProtocolBufferException;
import common.protocal.AuthRequestProto;

public class ImClientTestProto {

    public static void main(String[] args) throws InterruptedException, InvalidProtocolBufferException {

        AuthRequestProto.AuthRequest authRequest = create();
        byte[] byteArray = authRequest.toByteArray();
        System.out.println("序列化:" + byteArray.length);

        AuthRequestProto.AuthRequest authResponse = AuthRequestProto.AuthRequest.parseFrom(byteArray);
        System.out.println("反序列化:"+authResponse);

    }

    private static AuthRequestProto.AuthRequest create(){
        return AuthRequestProto.AuthRequest.newBuilder()
                .setUid("test_user")
                .setToken("test_user_token")
                .setTimestamp(System.currentTimeMillis())
                .build();
    }


}