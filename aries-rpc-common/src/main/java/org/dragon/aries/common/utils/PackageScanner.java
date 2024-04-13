package org.dragon.aries.common.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageScanner {
    public static List<Class<?>> getAnnotatedClasses(String packageName, Class<? extends Annotation> annotationClass)
            throws ClassNotFoundException {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources;
        try {
            loadClass(packageName, annotationClass, classLoader, packagePath, annotatedClasses);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return annotatedClasses;
    }

    private static void loadClass(String packageName, Class<? extends Annotation> annotationClass, ClassLoader classLoader, String packagePath, List<Class<?>> annotatedClasses) throws IOException, ClassNotFoundException {
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
                                loadClass(packageName + "." + fileName, annotationClass, classLoader, packagePath + "/" + fileName, annotatedClasses);
                            }
                        }
                    }
                }
            }
        }
    }
}
