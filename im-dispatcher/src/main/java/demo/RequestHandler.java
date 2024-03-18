package demo;

import demo.protocal.AuthRequestProto;
import demo.protocal.AuthResponseProto;

/**
 * @author anthony
 */
public class RequestHandler {

    public RequestHandler() {
    }

    static class Singleton {

        static final RequestHandler INSTANCE = new RequestHandler();

    }

    public static RequestHandler getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 处理认证请求
     */
    public AuthResponseProto.AuthResponse auth(AuthRequestProto.AuthRequest authRequest) {


        // 模拟验证账号

        AuthResponseProto.AuthResponse.Builder builder = AuthResponseProto.AuthResponse.newBuilder();
        try {

            String uid = authRequest.getUid();
            String token = authRequest.getToken();
            if (authBySSO(uid, token)) {
                builder.setStatus(Constants.RESPONSE_STATUS_OK);
//                builder.setErrorCode(Constants.RESPONSE_ERROR_CODE_OK);
                builder.setErrorMessage(Constants.RESPONSE_ERROR_MESSAGE_EMPTY);
            }else {
                builder.setStatus(Constants.RESPONSE_STATUS_ERROR);
                builder.setErrorCode(Constants.RESPONSE_ERROR_CODE_AUTH_FAIL);
                builder.setErrorMessage(Constants.RESPONSE_ERROR_MESSAGE_EMPTY);
            }
        } catch (Exception e) {
            builder.setStatus(Constants.RESPONSE_STATUS_ERROR);
            builder.setErrorCode(Constants.RESPONSE_ERROR_CODE_AUTH_EXCEPTION);
            builder.setErrorMessage(e.toString());
        }
        return builder.build();
    }

    private Boolean authBySSO(String uid, String token) {
        return true;
    }
}
