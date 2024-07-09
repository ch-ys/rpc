package org.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcConfig {
    private String host = "localhost";
    private Integer port = 8080;
    private String name = "easy-rpc";
}
