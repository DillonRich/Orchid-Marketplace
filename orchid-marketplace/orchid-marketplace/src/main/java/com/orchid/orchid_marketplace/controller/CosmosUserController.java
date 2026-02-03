package com.orchid.orchid_marketplace.controller;

import com.orchid.orchid_marketplace.dto.AuthResponse;
import com.orchid.orchid_marketplace.dto.LoginRequest;
import com.orchid.orchid_marketplace.dto.UserRequest;
import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.cosmos.CosmosUser;
import com.orchid.orchid_marketplace.security.JwtUtil;
import com.orchid.orchid_marketplace.service.CosmosUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Profile("cosmos")
public class CosmosUserController {

    @Autowired
    private CosmosUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequest request) {
        try {
            // Determine role from request
            Role role = "SELLER".equalsIgnoreCase(request.getRole()) ? Role.SELLER : Role.CUSTOMER;

            // Register user
            CosmosUser user = userService.registerUser(
                    request.getEmail(),
                    request.getFullName(),
                    request.getPassword(),
                    role
            );

            // Create UserDetails for JWT
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPasswordHash())
                    .roles(user.getRole().name())
                    .build();

            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails);

            // Return response
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setUsername(user.getFullName());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole().toString());
            response.setMessage("User registered successfully");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Find user
            CosmosUser user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create UserDetails for JWT
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPasswordHash())
                    .roles(user.getRole().name())
                    .build();

            // Generate token
            String token = jwtUtil.generateToken(userDetails);

            // Return response
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setUsername(user.getFullName());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole().toString());
            response.setMessage("Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage("Invalid credentials");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CosmosUser> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CosmosUser> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
