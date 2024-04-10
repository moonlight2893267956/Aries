package org.dragon.aries.core.proxy;

import org.dragon.aries.core.protocal.RpcStarter;

import java.lang.reflect.Proxy;

public class RpcProxyFactory {
    private final ProxyHandler proxyHandler;

    public RpcProxyFactory(RpcStarter rpcStarter) {
        proxyHandler = new ProxyHandler(rpcStarter);
    }

    public <T> T createProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxyHandler);
    }
}
