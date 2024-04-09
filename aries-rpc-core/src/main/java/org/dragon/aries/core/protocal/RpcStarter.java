package org.dragon.aries.core.protocal;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RpcStarter {
    private final static Logger log = LogManager.getLogger(RpcStarter.class);
    public void start() {
        start("localhost", 8888);
    }
    public abstract void start(String host, int port);
}
