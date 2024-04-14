package org.dragon.aries.core.protocal.netty.handler;

import org.dragon.aries.common.exception.RpcError;
import org.dragon.aries.common.exception.RpcException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.core.register.method.LocalMethodRegister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcMethodHandler {
    public Object handleMethod(String interfaceName, String methodName, Object[] params, Class<?>[] parameterTypes, String version) throws RuntimeException {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE);
        }
        Method declaredMethod = null;
        try {
            declaredMethod = aClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE);
        }
        LocalMethodRegister register = SingletonFactory.getInstance(LocalMethodRegister.class);
        Object instance = register.getInstance(interfaceName, version);
        if (instance == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        Object invoke;
        try {
            invoke = declaredMethod.invoke(instance, params);
        } catch (IllegalAccessException e) {
            throw new RpcException(RpcError.NO_PERMISSIONS_TO_INVOKE);
        } catch (InvocationTargetException e) {
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE);
        }
        return invoke;
    }
}
