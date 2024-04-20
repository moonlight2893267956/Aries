package org.dragon.aries.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.api.entity.Person;
import org.dragon.aries.api.service.HelloService;
import org.dragon.aries.common.config.ZookeeperConfiguration;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.core.proxy.RpcProxyFactory;

public class RpcClientStarter {
    private final static Logger log = LogManager.getLogger(RpcClientStarter.class);

    public static void main(String[] args) throws RegisterException {
        ZookeeperConfiguration configuration = SingletonFactory.getInstance(ZookeeperConfiguration.class);
        configuration.config();
        HelloService helloService = RpcProxyFactory.createProxy(HelloService.class, "v0.1");

        System.out.println(helloService.sayHello("wuxiangyi"));

        Person wuxiangyi = helloService.getPerson("wuxiangyi");
        System.out.println(wuxiangyi);
    }
}
