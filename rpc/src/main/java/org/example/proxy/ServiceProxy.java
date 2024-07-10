package org.example.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.example.config.RpcConfigHolder;
import org.example.model.RpcRequest;
import org.example.model.RpcResponse;
import org.example.serializer.Serializer;
import org.example.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 代理类
// 发送请求和请求处理
public class ServiceProxy implements InvocationHandler {
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 序列化器
        Serializer serializer = SerializerFactory.getSerializer(RpcConfigHolder.getRpcConfig().getSERIALIZER());

        // RpcRequest处理
        // RpcRequest构造
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParams(args);
        rpcRequest.setParamTypes(method.getParameterTypes());

        // 请求 响应处理
        // 地址注册中心实现
        try {
            byte[] rpcRequestSerialize = serializer.doSerialize(rpcRequest);
            HttpResponse response = HttpRequest.post("localhost:8080").body(rpcRequestSerialize).execute();
            byte[] RpcResponseBytes = response.bodyBytes();
            RpcResponse rpcResponse = serializer.deSerialize(RpcResponseBytes, RpcResponse.class);
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("RPC调用失败");
        }
        return null;
    }
}
