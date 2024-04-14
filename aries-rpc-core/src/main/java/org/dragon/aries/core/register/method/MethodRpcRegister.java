package org.dragon.aries.core.register.method;

import lombok.Getter;
import org.dragon.aries.common.utils.HashKit;
import org.dragon.aries.core.register.RpcRegister;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class MethodRpcRegister implements RpcRegister<String, Object> {
    private final Map<String, Object> interfaceRelInstance;
    public MethodRpcRegister() {
        this.interfaceRelInstance = new HashMap<>();
    }
    @Override
    public final void register(String key, Object value) {
        interfaceRelInstance.put(key, value);
    }

    public Object getInstance(String key) {
        return interfaceRelInstance.get(key);
    }

}
