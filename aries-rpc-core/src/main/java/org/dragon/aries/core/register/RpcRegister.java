package org.dragon.aries.core.register;

public interface RpcRegister<T> {
    void register(T data);

    Object getInstance(T data);

    void start();
}
