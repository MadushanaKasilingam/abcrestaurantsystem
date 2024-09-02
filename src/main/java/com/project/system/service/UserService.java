package com.project.system.service;

import com.project.system.model.User;

public interface UserService {

    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByUsername(String username) throws Exception;
}

