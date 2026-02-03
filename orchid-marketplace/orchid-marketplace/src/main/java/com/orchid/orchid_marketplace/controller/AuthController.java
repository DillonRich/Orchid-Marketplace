package com.orchid.orchid_marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.AuthResponse;
import com.orchid.orchid_marketplace.dto.AzureLoginRequest;
import com.orchid.orchid_marketplace.security.JwtUtil;
import com.orchid.orchid_marketplace.service.AzureAuthService;

import jakarta.validation.Valid;

/**
 * REST controller for authentication operations.
 * Handles Azure AD B2C login and token generation.
 */
@RestController
@RequestMapping("/api/auth")
@Profile("!cosmos")
public class AuthController {
    
    @Autowired
    private AzureAuthService azureAuthService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Authenticate user with Azure AD B2C ID token.
     * Validates the token and creates/updates user account.
     */
    @PostMapping("/azure-login")
    public ResponseEntity<AuthResponse> azureLogin(@Valid @RequestBody AzureLoginRequest request) {
        try {
            // Validate Azure B2C token and get/create user
            UserDetails userDetails = azureAuthService.authenticateWithAzure(request.getIdToken());
            
            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails);
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setUsername(userDetails.getUsername());
            authResponse.setEmail(userDetails.getUsername());
            authResponse.setRole(role);
            authResponse.setMessage("Azure login successful");
            
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage("Azure login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
