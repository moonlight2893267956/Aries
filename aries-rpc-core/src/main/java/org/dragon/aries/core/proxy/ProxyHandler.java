package org.dragon.aries.core.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.core.discovery.service.ZookeeperServiceDiscover;
import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.client.NettyRpcClient;
import org.dragon.aries.core.serialize.JsonSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {
    private final String version;

    public ProxyHandler(String version) {
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws JsonProcessingException {
        String interfaceName = method.getDeclaringClass().getName();
        ZookeeperServiceDiscover discover = new ZookeeperServiceDiscover();
        discover.discovery(interfaceName, version);
        Socket socket = discover.getSocket();

        RpcStarter rpcStarter = new NettyRpcClient(new JsonSerializer());
        rpcStarter.start(socket);

        Class<?>[] paramTypes = method.getParameterTypes();
        String methodName = method.getName();
        RpcRequest rpcRequest = new RpcRequest(RpcRequest.randomRequestId(), interfaceName, methodName, args, paramTypes, version, false);
        if (method.getReturnType().equals(String.class)) {
            return rpcStarter.send(rpcRequest).getData().toString().replaceAll("^\"|\"$", "");
        }
        ObjectMapper mapper = SingletonFactory.getInstance(ObjectMapper.class);
        return mapper.readValue(rpcStarter.send(rpcRequest).getData().toString(), method.getReturnType());
    }
}
