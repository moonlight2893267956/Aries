package org.dragon.aries.common.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

public class PackageScanner {
    public static Set<Class<?>> getAnnotatedClasses(String packageName, Class<? extends Annotation> annotationClass)
            throws ClassNotFoundException {
        Set<Class<?>> annotatedClasses = new HashSet<>();
        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources;
        try {
            loadClass(packageName, annotationClass, classLoader, packagePath, annotatedClasses, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return annotatedClasses;
    }

    private static void loadClass(String packageName, Class<? extends Annotation> annotationClass, ClassLoader classLoader, String packagePath, Set<Class<?>> annotatedClasses, int i) throws IOException, ClassNotFoundException {
        Enumeration<URL> resources;
        resources = classLoader.getResources(packagePath);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                File packageDir = new File(resource.getFile());
                if (packageDir.isDirectory()) {
                    File[] files = packageDir.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            String fileName = file.getName();
                            if (fileName.endsWith(".class")) {
                                String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                                Class<?> clazz = Class.forName(className);
                                if (clazz.isAnnotationPresent(annotationClass)) {
                                    annotatedClasses.add(clazz);
                                }
                            } else {
                                loadClass(packageName + "." + fileName, annotationClass, classLoader, packagePath + "/" + fileName, annotatedClasses, ++i);
                            }
                        }
                    }
                }
            }
        }
    }
}
