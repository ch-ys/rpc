package org.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.serializer.SerializerKeys;

// 默认配置
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcConfig {
    private String host = "localhost";
    private Integer port = 8080;
    private String name = "default-rpc";
    private boolean isMock = false;
    private String serializer = SerializerKeys.JDK_SERIALIZER;
}
