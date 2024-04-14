package org.dragon.aries.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.annotation.AriesScan;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.core.protocal.AriesServerManager;
import org.dragon.aries.server.common.SocketConstant;

@AriesScan(basePackages = "org.dragon.aries")
public class RpcServerStarter {
    private final static Logger log = LogManager.getLogger(RpcServerStarter.class);

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, RegisterException {
        boolean start = AriesServerManager.start(RpcServerStarter.class, SocketConstant.host, SocketConstant.port);
        if (start) {
            log.info("[RpcServerStarter] Server started successfully");
        }
    }
}
