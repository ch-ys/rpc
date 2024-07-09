package org.example;

import org.example.model.User;
import org.example.proxy.ServiceProxy;
import org.example.proxy.ServiceProxyFactory;
import org.example.service.UserService;

public class EasyConsumer {
    public static void main(String[] args) {
        User user = new User();
        user.setUserName("ceshi");

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
}