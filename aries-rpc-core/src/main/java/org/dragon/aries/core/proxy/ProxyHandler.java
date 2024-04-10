package org.dragon.aries.core.proxy;

import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.core.protocal.RpcStarter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {
    private final RpcStarter rpcStarter;

    public ProxyHandler(RpcStarter rpcStarter) {
        this.rpcStarter = rpcStarter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String interfaceName = method.getDeclaringClass().getName();
        Class<?>[] paramTypes = method.getParameterTypes();
        String methodName = method.getName();
        RpcRequest rpcRequest = new RpcRequest(RpcRequest.randomRequestId(), interfaceName, methodName, args, paramTypes, "v0.1", false);
        return rpcStarter.send(rpcRequest);
    }
}
