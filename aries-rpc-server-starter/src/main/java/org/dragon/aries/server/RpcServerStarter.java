package org.dragon.aries.server;

import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.server.NettyRpcServer;
import org.dragon.aries.core.serialize.JsonSerializer;

public class RpcServerStarter {

    public static void main(String[] args) {
        RpcStarter rpcServer = new NettyRpcServer(new JsonSerializer());
        rpcServer.start();
    }
}
