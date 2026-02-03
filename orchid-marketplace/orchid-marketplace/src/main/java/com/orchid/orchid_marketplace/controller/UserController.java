package com.orchid.orchid_marketplace.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.AuthResponse;
import com.orchid.orchid_marketplace.dto.LoginRequest;
import com.orchid.orchid_marketplace.dto.UserRequest;
import com.orchid.orchid_marketplace.dto.UserResponse;
import com.orchid.orchid_marketplace.mapper.UserMapper;
import com.orchid.orchid_marketplace.security.JwtUtil;
import com.orchid.orchid_marketplace.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Profile("!cosmos")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private com.orchid.orchid_marketplace.security.SpringUserDetailsService userDetailsService;

    @GetMapping
    public List<UserResponse> listAll() {
        return userService.getAllUsers().stream().map(UserMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return userService.getUserById(id).map(UserMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequest req) {
        com.orchid.orchid_marketplace.model.User user = UserMapper.toEntity(req);
        // store raw password in passwordHash field; service will encode
        user.setPasswordHash(req.getPassword());
        com.orchid.orchid_marketplace.model.User created = userService.createUser(user);
        
        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(created.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        
        // Get role from user
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUsername(created.getEmail());
        authResponse.setEmail(created.getEmail());
        authResponse.setRole(role);
        authResponse.setMessage("Registration successful");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
            
            // Generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setUsername(userDetails.getUsername());
            authResponse.setEmail(userDetails.getUsername());
            authResponse.setRole(role);
            authResponse.setMessage("Login successful");
            
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage("Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody UserRequest req) {
        com.orchid.orchid_marketplace.model.User user = UserMapper.toEntity(req);
        user.setPasswordHash(req.getPassword());
        com.orchid.orchid_marketplace.model.User created = userService.createUser(user);
        return UserMapper.toResponse(created);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UserRequest req) {
        com.orchid.orchid_marketplace.model.User detail = UserMapper.toEntity(req);
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            detail.setPasswordHash(req.getPassword());
        }
        com.orchid.orchid_marketplace.model.User updated = userService.updateUser(id, detail);
        return UserMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
