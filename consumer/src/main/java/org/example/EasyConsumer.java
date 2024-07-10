package org.example;

import org.example.config.RpcConfigHolder;
import org.example.model.User;
import org.example.proxy.ServiceProxyFactory;
import org.example.service.UserService;

public class EasyConsumer {
    public static void main(String[] args) {

        //配置对象初始化
        RpcConfigHolder.init();

        User user = new User();
        user.setUserName("ccccc");

        // 模拟远程调用服务
        // 代理类
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User getUser = userService.getUser(user);
        if (getUser != null) {
            System.out.println(getUser.getUserName());
        }else {
            System.out.println("调用错误：User not found");
        }
    }

//    public static void main(String[] args) {
//
//        RpcApplication.init();
//        // MocK模拟
//        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
//
//        System.out.println(userService.mock());
//    }
}