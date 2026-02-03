package com.orchid.orchid_marketplace.dto;

import jakarta.validation.constraints.NotBlank;

public class SellerResponseRequest {

    @NotBlank(message = "Response text is required")
    private String response;

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}
