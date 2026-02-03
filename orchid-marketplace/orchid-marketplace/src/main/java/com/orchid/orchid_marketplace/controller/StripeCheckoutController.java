package com.orchid.orchid_marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.StripeCheckoutSessionRequest;
import com.orchid.orchid_marketplace.dto.StripeCheckoutSessionResponse;
import com.orchid.orchid_marketplace.service.stripe.StripeCheckoutService;
import com.stripe.exception.StripeException;

import jakarta.validation.Valid;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/stripe")
@Validated
public class StripeCheckoutController {

    @Autowired
    private StripeCheckoutService stripeCheckoutService;

    @PostMapping("/checkout-session")
    public ResponseEntity<?> createCheckoutSession(@Valid @RequestBody StripeCheckoutSessionRequest request) {
        try {
            StripeCheckoutSessionResponse response = stripeCheckoutService.createCheckoutSession(
                request.getOrderId(),
                request.getSuccessUrl(),
                request.getCancelUrl());
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            // Keep details out of client responses; log handling can be added later.
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("Stripe error while creating checkout session");
        }
    }
}
