package com.orchid.orchid_marketplace.config;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {

    private String secretKey;
    private String webhookSecret;

    // Stripe Connect (Standard) OAuth
    private String connectClientId;
    private String connectRedirectUri;

    // Platform fee as percent of item subtotal (default 10%).
    private BigDecimal platformFeePercent = BigDecimal.valueOf(10);

    // Listing fee amount in cents (default 25 cents) accrued and deducted later.
    private long listingFeeCents = 25;

    // Default currency.
    private String currency = "usd";

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public String getWebhookSecret() { return webhookSecret; }
    public void setWebhookSecret(String webhookSecret) { this.webhookSecret = webhookSecret; }

    public String getConnectClientId() { return connectClientId; }
    public void setConnectClientId(String connectClientId) { this.connectClientId = connectClientId; }

    public String getConnectRedirectUri() { return connectRedirectUri; }
    public void setConnectRedirectUri(String connectRedirectUri) { this.connectRedirectUri = connectRedirectUri; }

    public BigDecimal getPlatformFeePercent() { return platformFeePercent; }
    public void setPlatformFeePercent(BigDecimal platformFeePercent) { this.platformFeePercent = platformFeePercent; }

    public long getListingFeeCents() { return listingFeeCents; }
    public void setListingFeeCents(long listingFeeCents) { this.listingFeeCents = listingFeeCents; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
