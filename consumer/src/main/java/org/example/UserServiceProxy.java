package org.example;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import io.vertx.core.http.HttpServerRequest;
import org.example.model.RpcRequest;
import org.example.model.RpcResponse;
import org.example.model.User;
import org.example.serializer.JdkSerializer;
import org.example.service.UserService;

import java.io.IOException;

// 代理类
// 发送请求和请求处理
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        // 序列化器
        JdkSerializer jdkSerializer = new JdkSerializer();

        // RpcRequest处理
        // RpcRequest构造
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName(UserService.class.getName());
        rpcRequest.setMethodName("getUser");
        rpcRequest.setParams(new Object[]{user});
        rpcRequest.setParamTypes(new Class[]{User.class});
        // 请求 响应处理
        // 地址注册中心实现
        try {
            byte[] rpcRequestSerialize = jdkSerializer.serialize(rpcRequest);
            HttpResponse response = HttpRequest.post("localhost:8080").body(rpcRequestSerialize).execute();
            byte[] RpcResponseBytes = response.bodyBytes();
            RpcResponse rpcResponse = jdkSerializer.deserialize(RpcResponseBytes, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("RPC调用失败");
        }
        return null;
    }
}
