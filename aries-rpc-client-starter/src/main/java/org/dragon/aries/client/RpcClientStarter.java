package org.dragon.aries.client;

import org.dragon.aries.client.service.RpcClientService;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.core.protocal.AriesClientManager;

public class RpcClientStarter {
    public static void main(String[] args) throws RegisterException {
        AriesClientManager.start(RpcClientStarter.class);
        RpcClientService instance = SingletonFactory.getInstance(RpcClientService.class);
        instance.remoteProxy();
    }
}
