package org.dragon.aries.core.discovery.service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.common.exception.RpcException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.common.lb.LoadBalance;

import java.util.List;

public class ZookeeperServiceDiscover extends ServiceDiscover {
    private final static String SERVICE_PATH = "/aries/service";
    private final static String SPLIT = "/";
    private final LoadBalance loadBalance;

    public ZookeeperServiceDiscover(LoadBalance lb) {
        this.loadBalance = lb;
    }

    @Override
    public void discovery() {
    }

    public void discovery(String interfaceName, String version) {
        ZooKeeper instance = SingletonFactory.getInstance(ZooKeeper.class);
        String basicPath = SERVICE_PATH + SPLIT + interfaceName + SPLIT + version;
        try {
            List<String> children = instance.getChildren(basicPath, null);
            if (children.isEmpty()) {
                throw new RpcException("[ZookeeperServiceDiscover] No services found in ZooKeeper");
            }
            String node = this.loadBalance.select(children);
            String[] socketSplit = new String(instance.getData(basicPath + SPLIT + node, null, null)).split(":");
            setSocket(new Socket(socketSplit[0], Integer.parseInt(socketSplit[1])));
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
