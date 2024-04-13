package org.dragon.aries.core.discovery;

import java.util.HashMap;
import java.util.Map;

public abstract class ServiceDiscovery implements Discovery {
    protected Map<String, Class<?>> services;
    public Class<?> getServices(String className) {
        if (services == null) {
            return null;
        }
        return services.get(className);
    };
    protected void putService(String serviceName, Class<?> serviceClass) {
        if (services == null) {
            services = new HashMap<String, Class<?>>();
        }
        services.put(serviceName, serviceClass);
    }
}
