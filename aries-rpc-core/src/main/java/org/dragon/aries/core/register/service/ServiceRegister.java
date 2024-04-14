package org.dragon.aries.core.register.service;

import lombok.Getter;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.common.entity.bo.RpcRegisterService;
import org.dragon.aries.core.register.RpcRegister;

@Getter
public abstract class ServiceRegister implements RpcRegister<RpcRegisterService, Socket> {
    private final Socket socket;
    public ServiceRegister(String host, Integer port) {
        socket = new Socket(host, port);
    }
}
