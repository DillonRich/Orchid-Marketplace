package com.orchid.orchid_marketplace.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for Azure AD B2C authentication.
 * Contains the ID token from Azure B2C for backend verification.
 */
public class AzureLoginRequest {
    
    @NotBlank(message = "ID token is required")
    private String idToken;
    
    // Constructors
    
    public AzureLoginRequest() {}
    
    public AzureLoginRequest(String idToken) {
        this.idToken = idToken;
    }
    
    // Getters and Setters
    
    public String getIdToken() {
        return idToken;
    }
    
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
