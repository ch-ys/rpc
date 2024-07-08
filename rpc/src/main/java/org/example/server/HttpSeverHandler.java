package org.example.server;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import org.example.model.RpcRequest;
import org.example.model.RpcResponse;
import org.example.registry.LocalRegistry;
import org.example.serializer.JdkSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpSeverHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest event) {
        // 序列化器
        JdkSerializer jdkSerializer = new JdkSerializer();

        // 消息处理
        event.bodyHandler(body -> {

            // vertx 缓存数据处理
            byte[] bytes = body.getBytes();

            //HttpServerRequest -> RpcRequest
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = jdkSerializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                System.out.println("Error deserializing RpcRequest");
                throw new RuntimeException(e);
            }

            // RpcResponse处理
            //构造RpcResponse
            RpcResponse rpcResponse = null;

            // 空RpcRequest 提前返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("Invalid RpcRequest");
                doResponse(rpcResponse,jdkSerializer);
                return;
            }

            // 非空 RpcRequest
            // 反射调用方法
            // 本地注册器npe处理
            Class<?> serviceClass = LocalRegistry.getService(rpcRequest.getServiceName());
            if (serviceClass == null) {
                rpcResponse.setMessage("Service not found");
                doResponse(rpcResponse,jdkSerializer);
                return;
            }
            try {
                Method method = serviceClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object invoke = method.invoke(rpcRequest.getParamTypes());
            } catch (Exception e) {
                System.out.println("No such method: " + rpcRequest.getMethodName());
                throw new RuntimeException(e);
            }


        });

    }

    // 添加返回信息 HttpServerResponse
    // 序列化
    // vertx 缓存化
    public void doResponse(RpcResponse rpcResponse,JdkSerializer jdkSerializer) {

    }
}
