package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.StripeConnectAuthorizeRequest;
import com.orchid.orchid_marketplace.dto.StripeConnectAuthorizeResponse;
import com.orchid.orchid_marketplace.dto.StripeConnectCallbackResponse;
import com.orchid.orchid_marketplace.service.stripe.StripeConnectService;

import jakarta.validation.Valid;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/stripe/connect")
@Validated
public class StripeConnectController {

    private final StripeConnectService stripeConnectService;

    public StripeConnectController(StripeConnectService stripeConnectService) {
        this.stripeConnectService = stripeConnectService;
    }

    @PostMapping("/authorize")
    public ResponseEntity<?> authorize(@Valid @RequestBody StripeConnectAuthorizeRequest request) {
        try {
            StripeConnectAuthorizeResponse resp = stripeConnectService.createAuthorizeUrl(request.getStoreId(), request.getReturnUrl());
            return ResponseEntity.ok(resp);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, @RequestParam("state") UUID state) {
        try {
            StripeConnectCallbackResponse resp = stripeConnectService.handleCallback(code, state);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/connected")
    public ResponseEntity<?> connected(@RequestParam("storeId") UUID storeId) {
        return ResponseEntity.ok(stripeConnectService.isStoreConnected(storeId));
    }
}
