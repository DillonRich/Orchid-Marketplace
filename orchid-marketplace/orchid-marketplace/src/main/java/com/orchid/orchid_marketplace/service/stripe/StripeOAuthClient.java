package com.orchid.orchid_marketplace.service.stripe;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchid.orchid_marketplace.config.StripeProperties;

@Component
public class StripeOAuthClient {

    private static final URI TOKEN_URI = URI.create("https://connect.stripe.com/oauth/token");

    private final StripeProperties stripeProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public StripeOAuthClient(StripeProperties stripeProperties, ObjectMapper objectMapper) {
        this.stripeProperties = stripeProperties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    public String exchangeAuthorizationCodeForAccountId(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }

        String secretKey = stripeProperties.getSecretKey();
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("Stripe secret key is not configured");
        }

        String redirectUri = stripeProperties.getConnectRedirectUri();

        StringBuilder form = new StringBuilder();
        form.append("client_secret=").append(url(secretKey));
        form.append("&code=").append(url(code));
        form.append("&grant_type=authorization_code");
        if (redirectUri != null && !redirectUri.isBlank()) {
            form.append("&redirect_uri=").append(url(redirectUri));
        }

        HttpRequest req = HttpRequest.newBuilder(TOKEN_URI)
            .timeout(Duration.ofSeconds(20))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(form.toString()))
            .build();

        HttpResponse<String> resp;
        try {
            resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException("Stripe OAuth token exchange failed (I/O)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Stripe OAuth token exchange interrupted", e);
        }

        String body = resp.body() == null ? "" : resp.body();
        if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
            throw new RuntimeException("Stripe OAuth token exchange failed with status " + resp.statusCode() + ": " + body);
        }

        try {
            JsonNode json = objectMapper.readTree(body);
            JsonNode stripeUserId = json.get("stripe_user_id");
            if (stripeUserId == null || stripeUserId.asText().isBlank()) {
                throw new RuntimeException("Stripe OAuth response missing stripe_user_id");
            }
            return stripeUserId.asText();
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Unable to parse Stripe OAuth response", e);
        }
    }

    private static String url(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
}
