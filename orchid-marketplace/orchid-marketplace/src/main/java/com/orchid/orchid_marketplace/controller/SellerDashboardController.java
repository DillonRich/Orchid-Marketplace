package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.SellerDashboardDTO;
import com.orchid.orchid_marketplace.service.SellerDashboardService;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/sellers")
@Validated
public class SellerDashboardController {

    private final SellerDashboardService dashboardService;

    public SellerDashboardController(SellerDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{storeId}/dashboard")
    public ResponseEntity<SellerDashboardDTO> getDashboard(@PathVariable UUID storeId) {
        SellerDashboardDTO dashboard = dashboardService.getDashboard(storeId);
        return ResponseEntity.ok(dashboard);
    }
}
