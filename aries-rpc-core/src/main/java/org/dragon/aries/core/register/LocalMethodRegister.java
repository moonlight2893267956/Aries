package org.dragon.aries.core.register;

import  org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.bo.RpcRegisterInterface;
import org.dragon.aries.common.utils.HashKit;

import java.util.HashMap;
import java.util.Map;

public class LocalMethodRegister implements RpcRegister<RpcRegisterInterface> {
    private final static Logger log = LogManager.getLogger(LocalMethodRegister.class);
    private Map<String, Object> interfaceRelInstance;

    public LocalMethodRegister() {
        this.interfaceRelInstance = new HashMap<>();
    }

    @Override
    public void register(RpcRegisterInterface value) {
        String regisKey = HashKit.sha512(value.getInterfaceName() + "_" + value.getVersion());
        if (interfaceRelInstance.containsKey(regisKey)) {
            log.warn("[LocalMethodRegister] interface {}_{} already registered, replacing it", value.getInterfaceName(), value.getVersion());
        }
        interfaceRelInstance.put(regisKey, value.getInstance());
        log.info("[LocalMethodRegister] interface {}_{} registered", value.getInterfaceName(), value.getVersion());
    }

    @Override
    public Object getInstance(RpcRegisterInterface data) {
        return this.interfaceRelInstance.get(HashKit.sha512(data.getInterfaceName() + "_" + data.getVersion()));
    }
}
