package org.dragon.aries.common.config;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.common.fatory.SingletonFactory;

import java.io.IOException;

@Data
public class ZookeeperConfiguration {
    private final static Logger log = LogManager.getLogger(ZookeeperConfiguration.class);
    public String zkServer = "k8s-master:2181,k8s-slave1:2181,k8s-slave2:2181";
    public Integer sessionTimeout = 5000;
    public void config() throws RegisterException {
        try {
            ZooKeeper instance = new ZooKeeper(zkServer, sessionTimeout, null);
            SingletonFactory.registerInstance(ZooKeeper.class, instance);
        } catch (IOException e) {
            log.error("[ZookeeperConfiguration] connect error, connect url: {}" , zkServer);
            throw new RegisterException("[Register Error] zookeeper connect error");
        }
        log.info("[ZookeeperConfiguration] connect success");
    }
}
