package org.dragon.aries.core.protocal;


import io.netty.channel.Channel;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.RpcResponse;

public abstract class RpcStarter {
    public Channel start() {
        return start("localhost", 8888);
    }
    public abstract Channel start(String host, int port);
    public abstract RpcResponse<?> send(RpcRequest request);
}
