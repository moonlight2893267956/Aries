package org.dragon.aries.client;

import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.client.NettyRpcClient;
import org.dragon.aries.core.serialize.JsonSerializer;

public class NettyClientStarter {

    public static void main(String[] args) {
        RpcStarter rpcStarter = new NettyRpcClient(new JsonSerializer());
        rpcStarter.start();
    }
}
