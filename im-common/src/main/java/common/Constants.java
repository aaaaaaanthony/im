package common;

public class Constants {

    /**
     * 消息类型是请求
     */
    public static final int MESSAGE_TYPE_REQUEST = 1;
    /**
     * 消息类型是响应
     */
    public static final int MESSAGE_TYPE_RESPONSE = 2;

    // 消息头的长度
    public static final int HEADER_LENGTH = 24;
    // app sdk版本号
    public static final int APP_SDK_VERSION_1 = 1;
    // 请求类型:用户认证
    public static final int REQUEST_TYPE_AUTH = 1;
    // 消息顺序
    public static final int SEQUENCE_DEFAULT = 1;
    public static final byte[] DELIMIT = "$_".getBytes();

    // 响应状态 正常
    public static final int RESPONSE_STATUS_OK = 1;

    // 响应状态 异常
    public static final int RESPONSE_STATUS_ERROR = 2;

    // 响应状态 未知
    public static final int RESPONSE_ERROR_STATUS_UNKNOWN = -1;

    // 响应异常信息
    public static final String RESPONSE_ERROR_MESSAGE_EMPTY = "";

    // 认证异常
    public static final int RESPONSE_ERROR_CODE_AUTH_EXCEPTION = 2;

    // 认证失败
    public static final int RESPONSE_ERROR_CODE_AUTH_FAIL = 1;
}
