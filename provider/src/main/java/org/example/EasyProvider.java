package org.example;

import org.example.config.RpcApplication;
import org.example.registry.LocalRegistry;
import org.example.server.VertxHttpServer;
import org.example.service.UserService;

import java.io.IOException;

public class EasyProvider {
    public static void main(String[] args) throws IOException {

        //配置初始化
        RpcApplication.init();

        // 本地注册器（映射实现类）
        LocalRegistry.addService(UserService.class.getName(),UserServiceImpl.class);

        // web服务
        // 请求处理(服务类内部封装)
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(RpcApplication.getRpcConfig().getPort());

        System.out.println("开始提供服务");
    }
}