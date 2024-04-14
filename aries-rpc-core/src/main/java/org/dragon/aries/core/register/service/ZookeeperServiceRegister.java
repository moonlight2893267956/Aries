package org.dragon.aries.core.register.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.dragon.aries.common.config.ZookeeperConfiguration;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.common.entity.bo.RpcRegisterService;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.common.fatory.SingletonFactory;

import java.nio.charset.StandardCharsets;

public class ZookeeperServiceRegister extends ServiceRegister {
    private final static Logger log = LogManager.getLogger(ZookeeperConfiguration.class);
    private final ZooKeeper zooKeeper;
    private final static String SPLIT = "/";
    private final static String ROOT_PATH = "/aries";
    private final static String SERVICE_PATH = "/service";

    public ZookeeperServiceRegister(String host, Integer port) throws RegisterException {
        super(host, port);
        ZookeeperConfiguration configuration = SingletonFactory.getInstance(ZookeeperConfiguration.class);
        configuration.config();
        zooKeeper = SingletonFactory.getInstance(ZooKeeper.class);
    }

    public void registerService(RpcRegisterService service) {
        this.register(service, getSocket());
    }

    @Override
    public final void register(RpcRegisterService key, Socket data) {
        try {
            // 创建项目根永久节点
            if (zooKeeper.exists(ROOT_PATH, false) == null) {
                zooKeeper.create(ROOT_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("Create zookeeper root node Success");
            }
            // 创建服务注册永久节点
            if (zooKeeper.exists(ROOT_PATH + SERVICE_PATH, false) == null) {
                zooKeeper.create(ROOT_PATH + SERVICE_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("Create zookeeper service Success");
            }
            // 创建注册服务节点
            String servicePath = ROOT_PATH + SERVICE_PATH + SPLIT + key.getServiceName();
            if (zooKeeper.exists(servicePath, false) == null) {
                zooKeeper.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("Create zookeeper service {} Success", key.getServiceName());
            }
            // 创建服务版本节点
            String versionPath = servicePath + SPLIT + key.getVersion();
            if (zooKeeper.exists(versionPath, false) == null) {
                zooKeeper.create(versionPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("Create zookeeper version {} Success", key.getVersion());
            }

            // 创建服务端socket相关临时有序节点
            String socket = data.getHost() + ":" + data.getPort();
            String childPath = versionPath + SPLIT + "node_";
            zooKeeper.create(childPath,
                    socket.getBytes(StandardCharsets.UTF_8),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("Create zookeeper temperature node Success");
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
