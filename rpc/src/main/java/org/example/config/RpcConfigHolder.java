package org.example.config;

import org.example.constant.RpcConstant;
import org.example.utils.ConfigUtils;

// 维护单一配置对象
public class RpcConfigHolder {
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
    // 双锁
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcConfigHolder.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
