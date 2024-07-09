package org.example.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.example.model.RpcRequest;
import org.example.model.RpcResponse;
import org.example.registry.LocalRegistry;
import org.example.serializer.JdkSerializer;

import java.io.IOException;
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
            RpcResponse rpcResponse = new RpcResponse();

            // 空RpcRequest 提前返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("Invalid RpcRequest");
                doResponse(event,rpcResponse,jdkSerializer);
                return;
            }

            // 非空 RpcRequest
            // 反射调用方法
            // 本地注册器npe处理
            Class<?> serviceClass = LocalRegistry.getService(rpcRequest.getServiceName());
            if (serviceClass == null) {
                rpcResponse.setMessage("Service not found");
                doResponse(event,rpcResponse,jdkSerializer);
                return;
            }
            try {
                Method method = serviceClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object invoke = method.invoke(serviceClass.newInstance(),rpcRequest.getParams());
                // 封装rpcResponse
                rpcResponse.setData(invoke);
                rpcResponse.setMessage("ok");
                rpcResponse.setDataType(method.getReturnType());
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
            }
            doResponse(event,rpcResponse,jdkSerializer);
        });
    }

    // 添加返回信息 HttpServerResponse
    // 返回对象序列化(类对象传输需求)
    // 返回对象缓存化（vertx的特性需求）
    public void doResponse(HttpServerRequest request,RpcResponse rpcResponse,JdkSerializer jdkSerializer) {
        HttpServerResponse response = request.response().
                putHeader("Content-Type", "application/json");
        try {
            byte[] serialize = jdkSerializer.serialize(rpcResponse);
            response.end(Buffer.buffer(serialize));
        } catch (IOException e) {
            e.printStackTrace();
            response.end(Buffer.buffer());
        }
    }
}
