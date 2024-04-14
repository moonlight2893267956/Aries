package org.dragon.aries.core.discovery.register;

import org.dragon.aries.core.discovery.Discovery;

import java.util.HashMap;
import java.util.Map;

public abstract class RegisterDiscovery implements Discovery {
    protected Map<String, Class<?>> services;
    public Class<?> getService(String className) {
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
