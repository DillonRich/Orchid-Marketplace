package com.orchid.orchid_marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.UserRepository;
import com.orchid.orchid_marketplace.security.SpringUserDetailsService;

/**
 * Service for Azure AD B2C authentication.
 * Validates Azure tokens and creates/retrieves user accounts.
 */
@Service
@Profile("!cosmos")
public class AzureAuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SpringUserDetailsService userDetailsService;
    
    /**
     * Authenticate user with Azure AD B2C ID token.
     * Validates the token and creates/updates user account.
     * 
     * @param idToken Azure AD B2C ID token
     * @return UserDetails for JWT generation
     */
    public UserDetails authenticateWithAzure(String idToken) {
        // TODO: Implement actual Azure AD B2C token validation
        // For now, this is a placeholder implementation
        // You would typically:
        // 1. Validate the token signature with Azure public keys
        // 2. Verify token claims (iss, aud, exp, etc.)
        // 3. Extract user information (email, name, etc.)
        
        // Placeholder: Extract email from token (in real implementation, parse JWT)
        // For MVP, assuming token validation is handled by frontend
        String email = extractEmailFromToken(idToken);
        String fullName = extractNameFromToken(idToken);
        
        // Find or create user
        User user = userRepository.findByEmail(email).orElse(null);
        
        if (user == null) {
            // Create new user from Azure profile
            user = new User();
            user.setEmail(email);
            user.setFullName(fullName != null ? fullName : email);
            user.setRole(Role.CUSTOMER);
            user.setPasswordHash("AZURE_SSO"); // No password for SSO users
            user = userService.createUser(user);
        }
        
        // Load and return UserDetails
        return userDetailsService.loadUserByUsername(user.getEmail());
    }
    
    /**
     * Extract email from Azure ID token.
     * In production, this should parse and validate the JWT.
     */
    private String extractEmailFromToken(String idToken) {
        // TODO: Implement proper JWT parsing and validation
        // For now, returning placeholder
        // In production, use com.auth0:java-jwt or io.jsonwebtoken:jjwt
        return "azure.user@example.com";
    }
    
    /**
     * Extract name from Azure ID token.
     * In production, this should parse and validate the JWT.
     */
    private String extractNameFromToken(String idToken) {
        // TODO: Implement proper JWT parsing and validation
        return "Azure User";
    }
}
