package org.dragon.aries.server;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class TestApplication {
    public static Integer sessionTimeout = 10000;
    public static String zkServer = "k8s-master:2181,k8s-slave1:2181,k8s-slave2:2181";

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException, IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(zkServer, sessionTimeout, null);
        Stat exists = zooKeeper.exists("/aries", null);
        System.out.println(exists);
    }
}
