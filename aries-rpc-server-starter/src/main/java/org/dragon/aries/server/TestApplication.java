package org.dragon.aries.server;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class TestApplication {
    public static Integer sessionTimeout = 10000;
    public static String zkServer = "k8s-master:2181,k8s-slave1:2181,k8s-slave2:2181";

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException, IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(zkServer, sessionTimeout, null);
        List<String> children = zooKeeper.getChildren("/aries/service/org.dragon.aries.api.service.SayService/default", null);
        System.out.println(children);
    }
}
