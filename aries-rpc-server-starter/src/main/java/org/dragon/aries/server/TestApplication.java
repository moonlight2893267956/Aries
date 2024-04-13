package org.dragon.aries.server;

import org.dragon.aries.common.annotation.AriesService;
import org.dragon.aries.common.utils.PackageScanner;

import java.util.List;

public class TestApplication {
    public static void main(String[] args) throws ClassNotFoundException {
        List<Class<?>> annotatedClasses = PackageScanner.getAnnotatedClasses("org.dragon.aries", AriesService.class);
        System.out.println("data => " + annotatedClasses);
        for (Class<?> annotatedClass : annotatedClasses) {
            Class<?>[] interfaces = annotatedClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                System.out.println(anInterface.getSimpleName());
            }
        }
    }
}
