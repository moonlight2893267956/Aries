package org.dragon.aries.core.protocal;

import org.dragon.aries.common.annotation.AriesScan;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.common.entity.bo.RpcRegisterInterface;
import org.dragon.aries.common.entity.bo.RpcRegisterService;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.core.discovery.service.AriesServiceDiscovery;
import org.dragon.aries.core.protocal.netty.server.NettyRpcServer;
import org.dragon.aries.core.register.method.LocalMethodRegister;
import org.dragon.aries.core.register.service.ZookeeperServiceRegister;
import org.dragon.aries.core.serialize.JsonSerializer;

import java.util.Map;

public class AriesServerManager {
    public static boolean start(Class<?> clazz, String host, Integer port) throws ClassNotFoundException, RegisterException, InstantiationException, IllegalAccessException {
        String[] registerPaths = clazz.getAnnotation(AriesScan.class).basePackages();

        // 服务注册
        for (String registerPath : registerPaths) {
            register(host, port, registerPath);
        }

        // 启动 Netty 务端
        new Thread(() -> {
            RpcStarter rpcServer = new NettyRpcServer(new JsonSerializer());
            rpcServer.start(new Socket(host, port));
        }).start();

        return true;
    }

    private static void register(String host, Integer port, String registerPath) throws ClassNotFoundException, RegisterException, InstantiationException, IllegalAccessException {
        AriesServiceDiscovery discovery = new AriesServiceDiscovery(registerPath);
        discovery.discovery();
        SingletonFactory.registerInstance(AriesServiceDiscovery.class, discovery);
        ZookeeperServiceRegister register = new ZookeeperServiceRegister(host, port);
        LocalMethodRegister methodRegister = SingletonFactory.getInstance(LocalMethodRegister.class);
        for (Map.Entry<String, Class<?>> entry : discovery.getServices().entrySet()) {
            String key = entry.getKey();
            int index = key.lastIndexOf(AriesServiceDiscovery.VERSION_SPLIT);
            String serviceName = key.substring(0, index);
            String version = key.substring(index + 1);

            register.registerService(RpcRegisterService.builder().serviceName(serviceName).version(version).build());
            methodRegister.registerMethod(RpcRegisterInterface.builder().interfaceName(serviceName).version(version).instance(entry.getValue().newInstance()).build());
        }
    }
}
