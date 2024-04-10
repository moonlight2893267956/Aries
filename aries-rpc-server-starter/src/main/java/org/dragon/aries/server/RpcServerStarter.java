package org.dragon.aries.server;

import org.dragon.aries.api.service.HelloService;
import org.dragon.aries.common.entity.bo.RpcRegisterInterface;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.server.NettyRpcServer;
import org.dragon.aries.core.register.LocalMethodRegister;
import org.dragon.aries.core.serialize.JsonSerializer;
import org.dragon.aries.server.service.HelloServiceImpl;

public class RpcServerStarter {

    public static void main(String[] args) {
        // 服务注册
        LocalMethodRegister register = SingletonFactory.getInstance(LocalMethodRegister.class);
        register.register(RpcRegisterInterface.builder().interfaceName(HelloService.class.getName()).version("v0.1").instance(new HelloServiceImpl()).build());

        RpcStarter rpcServer = new NettyRpcServer(new JsonSerializer());
        rpcServer.start();
    }
}
