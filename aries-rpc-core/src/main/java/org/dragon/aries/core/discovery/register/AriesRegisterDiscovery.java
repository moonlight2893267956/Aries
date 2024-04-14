package org.dragon.aries.core.discovery.register;

import org.dragon.aries.common.annotation.AriesService;
import org.dragon.aries.common.utils.PackageScanner;

import java.util.Map;
import java.util.Set;

public class AriesRegisterDiscovery extends RegisterDiscovery {
    private final String basePackage;
    public static final String VERSION_SPLIT = "/";

    public AriesRegisterDiscovery(String basePackage) {
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
