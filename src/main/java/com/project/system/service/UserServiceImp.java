package com.project.system.service;

import com.project.system.config.JwtProvider;
import com.project.system.model.USER_ROLE;
import com.project.system.model.User;
import com.project.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String username = jwtProvider.getUsernameFromJwtToken(jwt);

        // Check if the username is the hardcoded admin user
        if ("maduk".equals(username)) {
            User adminUser = new User();
            adminUser.setUsername("maduk");
            adminUser.setPassword(new BCryptPasswordEncoder().encode("000")); // Match with your encoded password
            adminUser.setRole(USER_ROLE.ROLE_ADMIN);
            return adminUser;
        }
        User user=findUserByUsername(username);
        return user;
    }

    @Override
    public User findUserByUsername(String username) throws Exception {
        User user=userRepository.findByUsername(username);

        if(user==null){
            throw new Exception("user not found");
        }
        return user;
    }

@Override
public User findUserById(Long id) throws Exception {
    return userRepository.findById(id)
            .orElseThrow(() -> new Exception("User not found"));
}}
