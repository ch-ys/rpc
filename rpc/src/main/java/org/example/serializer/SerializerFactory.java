package org.example.serializer;


import java.util.HashMap;
import java.util.Map;

// 序列化工厂
public class SerializerFactory {
  private static final Map<String, Serializer> SERIALIZER_MAP = new HashMap<String, Serializer>(){{
      put("JSON", new JsonSerializer());
      put("JDK", new JdkSerializer());
  }};

  private static final Serializer DEFAULT_SERIALIZER = SERIALIZER_MAP.get("JDK");

  public static Serializer getSerializer(String key) {
      return SERIALIZER_MAP.get(key);
  }
}
