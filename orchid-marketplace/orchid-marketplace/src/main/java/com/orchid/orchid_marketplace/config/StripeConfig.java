package com.orchid.orchid_marketplace.config;

import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

@Configuration
public class StripeConfig {

    public StripeConfig(StripeProperties properties) {
        String key = properties.getSecretKey();
        if (key != null && !key.isBlank()) {
            Stripe.apiKey = key;
        }
    }
}
