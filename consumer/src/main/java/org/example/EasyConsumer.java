package org.example;

import org.example.config.RpcApplication;
import org.example.config.RpcConfig;
import org.example.model.User;
import org.example.proxy.ServiceProxyFactory;
import org.example.service.UserService;

public class EasyConsumer {
//    public static void main(String[] args) {
//        User user = new User();
//        user.setUserName("ccccc");
//
//        // 模拟远程调用服务
//        // 代理类
//        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
//
//        User getUser = userService.getUser(user);
//        if (getUser != null) {
//            System.out.println(getUser.getUserName());
//        }else {
//            System.out.println("调用错误：User not found");
//        }
//    }

    public static void main(String[] args) {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println(rpcConfig);
    }


}