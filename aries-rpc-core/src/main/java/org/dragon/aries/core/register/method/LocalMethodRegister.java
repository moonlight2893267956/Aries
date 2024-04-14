package org.dragon.aries.core.register.method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.bo.RpcRegisterInterface;
import org.dragon.aries.common.utils.HashKit;

public class LocalMethodRegister extends MethodRpcRegister {
    private final static Logger log = LogManager.getLogger(LocalMethodRegister.class);

    private String generateKey(String interfaceName, String version) {
        return HashKit.sha512(interfaceName + "_" + version);
    }

    public void registerMethod(RpcRegisterInterface registerInterface) {
        String regisKey = generateKey(registerInterface.getInterfaceName() , registerInterface.getVersion());
        if (getInterfaceRelInstance().containsKey(regisKey)) {
            log.warn("[LocalMethodRegister] interface {}_{} already registered, replacing it", registerInterface.getInterfaceName(), registerInterface.getVersion());
        }
        register(regisKey, registerInterface.getInstance());
        log.info("[LocalMethodRegister] interface {}_{} registered", registerInterface.getInterfaceName(), registerInterface.getVersion());
    }

    public Object getInstance(String interfaceName, String version) {
        String key = generateKey(interfaceName, version);
        return getInstance(key);
    }
}
