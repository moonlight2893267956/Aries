package org.dragon.aries.core.discovery.service;

import org.dragon.aries.common.annotation.AriesService;
import org.dragon.aries.common.utils.PackageScanner;

import java.util.Map;
import java.util.Set;

public class AriesServiceDiscovery extends ServiceDiscovery {
    private final String basePackage;
    public static final String VERSION_SPLIT = "/";

    public AriesServiceDiscovery(String basePackage) {
        this.basePackage = basePackage;
    }

    public Class<?> getService(String className) {
        return super.getService(className);
    }

    public Map<String, Class<?>> getServices() {
        return super.services;
    }

    @Override
    public void discovery() throws ClassNotFoundException {
        Set<Class<?>> annotatedClasses = PackageScanner.getAnnotatedClasses(basePackage, AriesService.class);
        annotatedClasses.forEach(aClass -> {
            String interfaceName = aClass.getInterfaces()[0].getName();
            String version = aClass.getAnnotation(AriesService.class).version();
            putService(interfaceName + VERSION_SPLIT + version, aClass);
        });
    }
}
