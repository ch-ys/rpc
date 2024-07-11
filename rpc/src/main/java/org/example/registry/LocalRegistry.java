package org.example.registry;

import java.util.concurrent.ConcurrentHashMap;

//本地服务类注册器
public class LocalRegistry {
    private static final ConcurrentHashMap<String, Class<?>> registry = new ConcurrentHashMap<>();

    //本地注册器相关操作
    //本地服务的映射（针对于多服务rpc）
    public static void addService(String serviceName, Class<?> serviceClass) {
        registry.put(serviceName, serviceClass);
    }

    public static Class<?> getService(String serviceName) {
        return registry.get(serviceName);
    }

    public static void removeService(String serviceName) {
        registry.remove(serviceName);
    }
}
