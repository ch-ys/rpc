package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.constant.RpcConstant;
import org.example.utils.ConfigUtils;

// 维护单一配置对象

public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    // 指定配置
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        System.out.println("RpcApplication init" + rpcConfig.toString());
    }

    // 默认配置
    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    // 获取配置对象
    // 双锁 防止获取刚创建完都对象释放的锁
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
