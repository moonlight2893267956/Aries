package org.dragon.aries.core.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.RpcResponse;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.common.enumeration.ResponseCode;
import org.dragon.aries.common.exception.RpcException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.common.lb.RobinLoadBalance;
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
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            String interfaceName = method.getDeclaringClass().getName();
            ZookeeperServiceDiscover discover = new ZookeeperServiceDiscover(new RobinLoadBalance());
            discover.discovery(interfaceName, version);
            Socket socket = discover.getSocket();

            RpcStarter rpcStarter = new NettyRpcClient(new JsonSerializer());
            rpcStarter.start(socket);

            Class<?>[] paramTypes = method.getParameterTypes();
            String methodName = method.getName();
            RpcRequest rpcRequest = new RpcRequest(RpcRequest.randomRequestId(), interfaceName, methodName, args, paramTypes, version, false);
            RpcResponse<?> response = rpcStarter.send(rpcRequest);
            if (response.getStatusCode().equals(ResponseCode.FAIL.getCode())) {
                throw new RpcException("[ProxyHandler] remote service handler fail");
            }
            if (method.getReturnType().equals(String.class)) {
                return response.getData().toString().replaceAll("^\"|\"$", "");
            }
            ObjectMapper mapper = SingletonFactory.getInstance(ObjectMapper.class);
            return mapper.readValue(response.getData().toString(), method.getReturnType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
