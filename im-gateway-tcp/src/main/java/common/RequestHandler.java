package common;

import common.dispatcher.DispatcherInstance;
import common.dispatcher.DispatcherInstanceManager;
import common.protocal.AuthRequestProto;

public class RequestHandler {

    public RequestHandler() {
    }

    static class Singleton {
        private static final RequestHandler INSTANCE = new RequestHandler();
    }

    public static RequestHandler getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 认证请求处理逻辑,转发的逻辑
     * @param authRequest
     * @return
     */
    public void auth(AuthRequestProto.AuthRequest authRequest) {

        // 随机选一个分发实例
        DispatcherInstanceManager instance = DispatcherInstanceManager.getInstance();
        DispatcherInstance dispatcherInstance = instance.chooseDispatcherInstance();
        dispatcherInstance.auth(authRequest);

        // 这里一旦认证成功之后,就需要去维护session,也就是一个客户端的链接建立起来了
        // 一方面在自己本地内存里维护session,一方面去Redis里写入集中式管理的Session
    }
}
