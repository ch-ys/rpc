package org.example;

import org.example.config.RpcConfigHolder;
import org.example.registry.LocalRegistry;
import org.example.serializer.Serializer;
import org.example.server.VertxHttpServer;
import org.example.service.UserService;
import org.example.spi.SpiLoader;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;


public class test {
    public static void main(String[] args) throws IOException {
        // 测试加载器
        test();
    }
    public static void test() {
        Map<String, Class<?>> load = SpiLoader.load(Serializer.class);
        System.out.println(load);
    }
}