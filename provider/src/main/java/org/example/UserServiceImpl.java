package org.example;

import org.example.model.User;
import org.example.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println(user);
        return user;
    }

    @Override
    public String mock() {
        return "";
    }
}
