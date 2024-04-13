package org.dragon.aries.server;

import org.dragon.aries.common.annotation.AriesService;
import org.dragon.aries.common.utils.PackageScanner;
import org.dragon.aries.core.discovery.AriesServiceDiscovery;

import java.util.List;

public class TestApplication {
    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException {
        AriesServiceDiscovery discovery = new AriesServiceDiscovery("org.dragon");
        discovery.discovery();
        Class<?> helloService = discovery.getServices("HelloService");
        System.out.println(helloService);
    }
}
