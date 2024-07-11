package org.example.serializer;


import org.example.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

// 序列化工厂
public class SerializerFactory {
    // 加载class文件
    // 类加载时候创建
//    static {
//        SpiLoader.load(Serializer.class);
//    }

  private static final Serializer DEFAULT_SERIALIZER = getSerializer("JDK");

  public static Serializer getSerializer(String key) {
      return SpiLoader.getInstance2Lazy(Serializer.class,key);
  }
}
