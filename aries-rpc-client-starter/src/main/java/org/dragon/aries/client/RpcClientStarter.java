package org.dragon.aries.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.client.service.RpcClientService;
import org.dragon.aries.common.annotation.AriesComponent;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.core.protocal.AriesClientManager;

@AriesComponent
public class RpcClientStarter {
    private final static Logger log = LogManager.getLogger(RpcClientStarter.class);

    public static void main(String[] args) throws RegisterException {
        AriesClientManager.start(RpcClientStarter.class);
        RpcClientService instance = SingletonFactory.getInstance(RpcClientService.class);
        instance.remoteProxy();
    }
}
