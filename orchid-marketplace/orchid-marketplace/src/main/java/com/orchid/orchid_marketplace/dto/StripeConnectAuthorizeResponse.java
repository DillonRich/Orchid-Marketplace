package com.orchid.orchid_marketplace.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class StripeConnectAuthorizeResponse {
    private String url;
    private UUID state;
    private LocalDateTime expiresAt;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public UUID getState() { return state; }
    public void setState(UUID state) { this.state = state; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
