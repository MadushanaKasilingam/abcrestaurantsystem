package com.project.system.controller;

import com.project.system.model.User;
import com.project.system.service.UserService;
import com.project.system.config.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/profile")
    public ResponseEntity<User> findUserByJwtToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract JWT token from the Authorization header
            String jwtToken = authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : authorizationHeader;

            // Get user ID from JWT token
            Long userId = jwtProvider.getUserIdFromJwtToken(jwtToken);

            // Fetch user details using the user ID
            User user = userService.findUserById(userId);

            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception e) {
            // Handle exceptions, such as invalid token or user not found
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
