package org.example.proxy;


import org.example.config.RpcConfigHolder;

import java.lang.reflect.Proxy;

// 代理类
// 发送请求和请求处理
// 添加Mock功能
public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcConfigHolder.getRpcConfig().isMock()){
            return getMock(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    private static <T> T getMock(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
