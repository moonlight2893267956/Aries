package org.dragon.aries.core.protocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.annotation.AriesComponent;
import org.dragon.aries.common.annotation.AriesReference;
import org.dragon.aries.common.config.ZookeeperConfiguration;
import org.dragon.aries.common.entity.bo.CommonField;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.common.utils.PackageScanner;
import org.dragon.aries.core.proxy.RpcProxyFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AriesClientManager {
    private final static Logger log = LogManager.getLogger(AriesServerManager.class);

    public static void start(Class clazz) {
        try {
            // zookeeper 连接准备
            prepareZookeeper();
            // 实例对象注入
            Set<Class<?>> annotatedClasses = PackageScanner.getAnnotatedClasses(clazz.getPackage().getName(), AriesComponent.class);
            log.info("annotated classes: {}", annotatedClasses);
            instanceInjection(annotatedClasses);
        } catch (RegisterException e) {
            log.error("[AriesClientManager] Zookeeper register failed.", e);
        } catch (ClassNotFoundException e) {
            log.error("[AriesClientManager] Zookeeper class not found.", e);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("[AriesClientManager] Zookeeper instantiation failed.", e);
        }
    }

    private static void instanceInjection(Set<Class<?>> annotatedClasses) throws InstantiationException, IllegalAccessException {
        for (Class<?> instance : annotatedClasses) {
            Field[] declaredFields = instance.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                List<Annotation> annotations = Arrays.stream(declaredField.getDeclaredAnnotations())
                        .filter(obj -> obj instanceof AriesReference)
                        .collect(Collectors.toList());
                if (annotations.isEmpty()) {
                    continue;
                }
                log.info("[AriesClientManager] Instance field: {} of class: {}", declaredField.getName(), instance.getName());
                AriesReference annotation = declaredField.getAnnotation(AriesReference.class);
                Object proxy = RpcProxyFactory.createProxy(declaredField.getType(), CommonField.builder()
                        .version(annotation.version())
                        .retry(annotation.retry())
                        .retryInterval(annotation.retryInterval())
                        .timeout(annotation.timeout())
                        .callback(annotation.callback())
                        .build());
                Object o = instance.newInstance();
                declaredField.setAccessible(true);
                declaredField.set(o, proxy);

                SingletonFactory.registerInstance(o.getClass(), o);
            }
        }
    }

    private static void prepareZookeeper() throws RegisterException {
        ZookeeperConfiguration configuration = SingletonFactory.getInstance(ZookeeperConfiguration.class);
        configuration.config();
    }
}
