package org.dragon.aries.core.protocal;

public abstract class RpcServer {
    // rpc服务端启动入口
    public void start() {
        start("localhost", 8888);
    }

    public abstract void start(String host, int port);
}
