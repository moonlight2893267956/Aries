package org.dragon.aries.core.protocal;


import io.netty.channel.Channel;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.RpcResponse;
import org.dragon.aries.common.entity.Socket;

public abstract class RpcStarter {
    public Channel start(Socket socket) {
        return start(socket.getHost(), socket.getPort());
    }
    public abstract Channel start(String host, int port);
    public abstract RpcResponse<?> send(RpcRequest request, Long timeout);
}
