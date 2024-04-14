package org.dragon.aries.core.register;

public interface RpcRegister<K, V> {
    void register(K key, V data);
}
