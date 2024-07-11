package org.example.serializer;


import org.example.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

// 序列化工厂
public class SerializerFactory {
    // 加载class文件
    static {
        SpiLoader.load(Serializer.class);
    }
//  private static final Map<String, Serializer> SERIALIZER_MAP = new HashMap<String, Serializer>(){{
//      put("JSON", new JsonSerializer());
//      put("JDK", new JdkSerializer());
//  }};

  private static final Serializer DEFAULT_SERIALIZER = getSerializer("JDK");

  public static Serializer getSerializer(String key) {
      return SpiLoader.getInstance(Serializer.class,key);
  }
}
