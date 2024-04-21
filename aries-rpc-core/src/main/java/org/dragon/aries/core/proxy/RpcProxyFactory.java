package org.dragon.aries.core.proxy;

import org.dragon.aries.common.entity.bo.CommonField;

import java.lang.reflect.Proxy;

public class RpcProxyFactory {

    public static  <T> T createProxy(Class<T> clazz, CommonField field) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(field));
    }
}
