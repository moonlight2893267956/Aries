package org.dragon.aries.core.discovery;

import org.dragon.aries.common.annotation.AriesService;
import org.dragon.aries.common.utils.PackageScanner;

import java.util.List;
import java.util.Set;

public class AriesServiceDiscovery extends ServiceDiscovery{
    private final String basePackage;
    public AriesServiceDiscovery(String basePackage) {
        this.basePackage = basePackage;
    }

    public Class<?> getServices(String className) {
        return super.getServices(className);
    }

    @Override
    public void discovery() throws ClassNotFoundException {
        Set<Class<?>> annotatedClasses = PackageScanner.getAnnotatedClasses(basePackage, AriesService.class);
        annotatedClasses.forEach(aClass -> putService(aClass.getInterfaces()[0].getSimpleName(), aClass));
    }
}
