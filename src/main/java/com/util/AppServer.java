package com.util;

import com.datasourse.repos.UserRepository;

import com.services.UserService;

public class AppServer {




    public AppServer() {
        UserRepository userRepo = new UserRepository();
        UserService userService = new UserService(userRepo);
    }



}
