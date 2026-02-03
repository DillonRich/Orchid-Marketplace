package com.orchid.orchid_marketplace.service.stripe;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class StripeConnectServiceTest {

    @Test
    void buildAuthorizeUrl_containsExpectedParams() {
        UUID state = UUID.randomUUID();
        String url = StripeConnectService.buildAuthorizeUrl("ca_123", "http://localhost:8080/api/stripe/connect/callback", state);

        assertTrue(url.startsWith("https://connect.stripe.com/oauth/authorize?"));
        assertTrue(url.contains("response_type=code"));
        assertTrue(url.contains("client_id=ca_123"));
        assertTrue(url.contains("scope=read_write"));
        assertTrue(url.contains("redirect_uri="));
        assertTrue(url.contains("state="));
    }
}
