package org.dragon.aries.core.proxy;

import java.lang.reflect.Proxy;

public class RpcProxyFactory {

    public static  <T> T createProxy(Class<T> clazz, String version) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(version));
    }
}
