package org.dragon.aries.core.discovery.service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.common.fatory.SingletonFactory;

import java.util.List;
import java.util.Random;

public class ZookeeperServiceDiscover extends ServiceDiscover {
    private final static String SERVICE_PATH = "/aries/service";
    private final static String SPLIT = "/";
    @Override
    public void discovery() {
    }

    public void discovery(String interfaceName, String version) {
        ZooKeeper instance = SingletonFactory.getInstance(ZooKeeper.class);
        String basicPath = SERVICE_PATH + SPLIT + interfaceName + SPLIT + version;
        try {
            List<String> children = instance.getChildren(basicPath, null);
            String node = children.get(new Random().nextInt(children.size()));
            String[] socketSplit = new String(instance.getData(basicPath + SPLIT + node, null, null)).split(":");
            setSocket(new Socket(socketSplit[0], Integer.parseInt(socketSplit[1])));
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
