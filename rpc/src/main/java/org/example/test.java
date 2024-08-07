package org.example;

import org.example.config.RpcConfigHolder;
import org.example.serializer.Serializer;
import org.example.serializer.SerializerFactory;
import org.example.serializer.SerializerKeys;
import org.example.spi.SpiLoader;

import java.io.IOException;
import java.util.Map;


public class test {
    public static void main(String[] args) throws IOException {
//        // 测试加载器
//        testLoad();
//        // 测试获取
//        testGet();
        testGetByFactory();

    }
    public static void testLoad() {
        Map<String, Class<?>> load = SpiLoader.load(Serializer.class);
        System.out.println(load);
    }

    public static void testGet() {
        Serializer instance = SpiLoader.getInstance(Serializer.class, SerializerKeys.JSON_SERIALIZER);
        System.out.println(instance);
    }

    public static void testGetByFactory() {
        Serializer serializer = SerializerFactory.getSerializer(RpcConfigHolder.getRpcConfig().getSerializer());
        System.out.println(serializer);
    }
}