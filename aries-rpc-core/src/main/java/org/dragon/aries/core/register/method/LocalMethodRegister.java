package org.dragon.aries.core.register.method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.bo.RpcRegisterInterface;
import org.dragon.aries.common.utils.HashKit;

public class LocalMethodRegister extends MethodRpcRegister {
    private final static Logger log = LogManager.getLogger(LocalMethodRegister.class);

    public void registerMethod(RpcRegisterInterface registerInterface) {
        String regisKey = HashKit.sha512(registerInterface.getInterfaceName() + "_" + registerInterface.getVersion());
        if (getInterfaceRelInstance().containsKey(regisKey)) {
            log.warn("[LocalMethodRegister] interface {}_{} already registered, replacing it", registerInterface.getInterfaceName(), registerInterface.getVersion());
        }
        register(regisKey, registerInterface.getInstance());
        log.info("[LocalMethodRegister] interface {}_{} registered", registerInterface.getInterfaceName(), registerInterface.getVersion());
    }
}
