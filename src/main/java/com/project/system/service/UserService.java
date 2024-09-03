package com.project.system.service;

import com.project.system.model.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByUsername(String username) throws Exception;

    User findUserById(Long id) throws Exception; // New method to find a user by ID
}
