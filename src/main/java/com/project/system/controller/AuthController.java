package com.project.system.controller;

import com.project.system.config.JwtProvider;
import com.project.system.model.USER_ROLE;
import com.project.system.model.User;
import com.project.system.repository.UserRepository;
import com.project.system.request.LoginRequest;
import com.project.system.response.AuthResponse;
import com.project.system.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    // Admin adding Restaurant Owner
    @PostMapping("/addStaff")
    public ResponseEntity<AuthResponse> addStaff(@RequestBody User user) {
        AuthResponse authResponse = new AuthResponse();

        // Check if the current user is an admin
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            authResponse.setMessage("You do not have permission to perform this action");
            return new ResponseEntity<>(authResponse, HttpStatus.FORBIDDEN);
        }

        // Check if the username is already used
        if (userRepository.findByUsername(user.getUsername()) != null) {
            authResponse.setMessage("Username is already in use");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }

        // Validate required fields
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            authResponse.setMessage("Username and password are required and cannot be empty");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }

        // Create a new Staff User
        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setUsername(user.getUsername());
        createdUser.setRole(USER_ROLE.ROLE_STAFF);
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createdUser);

        authResponse.setMessage("Staff account created successfully");
        authResponse.setRole(savedUser.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PutMapping("/updateStaff/{id}")
    public ResponseEntity<AuthResponse> updateStaff(@PathVariable Long id, @RequestBody User user) {
        AuthResponse authResponse = new AuthResponse();

        // Check if the current user is an admin
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            authResponse.setMessage("You do not have permission to perform this action");
            return new ResponseEntity<>(authResponse, HttpStatus.FORBIDDEN);
        }

        // Check if the staff member exists
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            authResponse.setMessage("Staff member not found");
            return new ResponseEntity<>(authResponse, HttpStatus.NOT_FOUND);
        }

        // Update fields
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);

        authResponse.setMessage("Staff account updated successfully");
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @DeleteMapping("/deleteStaff/{id}")
    public ResponseEntity<AuthResponse> deleteStaff(@PathVariable Long id) {
        AuthResponse authResponse = new AuthResponse();

        // Check if the current user is an admin
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            authResponse.setMessage("You do not have permission to perform this action");
            return new ResponseEntity<>(authResponse, HttpStatus.FORBIDDEN);
        }

        // Check if the staff member exists
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            authResponse.setMessage("Staff member not found");
            return new ResponseEntity<>(authResponse, HttpStatus.NOT_FOUND);
        }

        userRepository.delete(existingUser);

        authResponse.setMessage("Staff account deleted successfully");
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @GetMapping("/getAllStaffs")
    public ResponseEntity<List<User>> getAllStaffs() {
        List<User> staffList = userRepository.findAll().stream()
                .filter(user -> USER_ROLE.ROLE_STAFF.equals(user.getRole()))
                .collect(Collectors.toList());

        if (staffList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }
    @GetMapping("/getStaffById/{id}")
    public ResponseEntity<User> getStaffById(@PathVariable Long id) {
        // Check if the current user is an admin
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Find the staff member by ID
        User staffMember = userRepository.findById(id).orElse(null);
        if (staffMember == null || !USER_ROLE.ROLE_STAFF.equals(staffMember.getRole())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(staffMember, HttpStatus.OK);
    }



    // Customer/Restaurant Owner Signup
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        AuthResponse authResponse = new AuthResponse();

        try {
            // Check if the username is already used
            if (userRepository.findByUsername(user.getUsername()) != null) {
                authResponse.setMessage("Username is already in use");
                return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
            }

            // Validate required fields
            if (user.getUsername() == null || user.getPassword() == null || user.getRole() == null || user.getEmail() == null) {
                authResponse.setMessage("Fields cannot be empty");
                return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
            }

            // Create a new User
            User createdUser = new User();
            createdUser.setEmail(user.getEmail());
            createdUser.setUsername(user.getUsername());
            createdUser.setRole(user.getRole());
            createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(createdUser);

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateToken(authentication);

            authResponse.setJwt(jwt);
            authResponse.setMessage("Registration successful");
            authResponse.setRole(savedUser.getRole());

            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

        } catch (Exception e) {
            authResponse.setMessage("Error occurred during registration");
            return new ResponseEntity<>(authResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // User Signin (Admin, Customer, Restaurant Owner)
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req) {
        AuthResponse authResponse = new AuthResponse();

        try {
            String username = req.getUsername();
            String password = req.getPassword();

            // Validate required fields
            if (username == null || password == null) {
                authResponse.setMessage("Username and password are required");
                return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
            }

            // Authenticate the user
            Authentication authentication = authenticate(username, password);

            // Retrieve the role of the authenticated user
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

            // Generate a JWT for the authenticated user
            String jwt = jwtProvider.generateToken(authentication);

            authResponse.setJwt(jwt);
            authResponse.setMessage("Login successful");
            authResponse.setRole(USER_ROLE.valueOf(role));

            return new ResponseEntity<>(authResponse, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            authResponse.setMessage("Invalid username or password");
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            authResponse.setMessage("Invalid username or password");
            return new ResponseEntity<>(authResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to authenticate a user
    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
