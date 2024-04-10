package org.dragon.aries.common.fatory;

import java.util.HashMap;
import java.util.Map;

public class SingletonFactory {
    private static final Map<Class<?> , Object> cache = new HashMap<>();
    private SingletonFactory() {}
    public static <T> T getInstance(Class<T> clazz) {
        Object o = cache.get(clazz);
        synchronized (clazz) {
            if (o == null) {
                try {
                    o = clazz.newInstance();
                    cache.put(clazz, o);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return clazz.cast(o);
    }
}
