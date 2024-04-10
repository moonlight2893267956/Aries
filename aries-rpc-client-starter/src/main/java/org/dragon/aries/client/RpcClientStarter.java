package org.dragon.aries.client;

import org.dragon.aries.api.service.HelloService;
import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.client.NettyRpcClient;
import org.dragon.aries.core.proxy.RpcProxyFactory;
import org.dragon.aries.core.serialize.JsonSerializer;

public class RpcClientStarter {

    public static void main(String[] args) {
        RpcStarter rpcStarter = new NettyRpcClient(new JsonSerializer());
        rpcStarter.start();

        HelloService helloService = new RpcProxyFactory(rpcStarter).createProxy(HelloService.class);
        System.out.println(helloService.sayHello("wuxiangyi"));
    }
}
