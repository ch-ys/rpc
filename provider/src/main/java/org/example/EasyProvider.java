package org.example;

import org.example.registry.LocalRegistry;
import org.example.server.VertxHttpServer;
import org.example.service.UserService;

import java.io.IOException;

public class EasyProvider {
    public static void main(String[] args) throws IOException {

        // 本地注册器（映射实现类）
        LocalRegistry.addService(UserService.class.getName(),UserServiceImpl.class);

        // web服务
        // 请求处理(服务类内部封装)
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(8080);

        System.out.println("开始提供服务");
    }
}