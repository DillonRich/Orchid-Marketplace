package com.orchid.orchid_marketplace.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.dto.SellerDashboardDTO;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.repository.ProductRepository;
import com.orchid.orchid_marketplace.repository.StoreRepository;

@Service
@Profile("!cosmos")
public class SellerDashboardService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public SellerDashboardService(
        StoreRepository storeRepository,
        ProductRepository productRepository
    ) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public SellerDashboardDTO getDashboard(UUID storeId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));

        SellerDashboardDTO dashboard = new SellerDashboardDTO();
        dashboard.setStoreId(storeId);
        dashboard.setStoreName(store.getStoreName());

        // Stripe connection status
        if (store.getSeller() != null && store.getSeller().getStripeConnectAccountId() != null) {
            dashboard.setStripeConnected(true);
            dashboard.setStripeAccountId(store.getSeller().getStripeConnectAccountId());
        } else {
            dashboard.setStripeConnected(false);
        }

        // Order metrics - will need custom repository queries for efficiency
        // For now using simple counts
        long totalOrders = 0;
        long pendingOrders = 0;
        long shippedOrders = 0;
        long deliveredOrders = 0;

        // Note: These queries should be optimized with proper JPA queries targeting seller's orders
        // This is a simplified version - in production, add custom methods to OrderRepository

        dashboard.setTotalOrders(totalOrders);
        dashboard.setPendingOrders(pendingOrders);
        dashboard.setShippedOrders(shippedOrders);
        dashboard.setDeliveredOrders(deliveredOrders);

        // Product metrics - use count if method exists, else use 0
        long totalProducts = 0;
        try {
            // totalProducts = productRepository.countByStoreId(storeId);
        } catch (Exception e) {
            // Method not available, use 0
        }
        dashboard.setTotalProducts(totalProducts);
        dashboard.setActiveProducts(totalProducts); // Assuming all are active
        dashboard.setOutOfStockProducts(0L);

        // Revenue metrics (would need sales ledger table for accurate data)
        dashboard.setTotalRevenue(BigDecimal.ZERO);
        dashboard.setMonthlyRevenue(BigDecimal.ZERO);
        dashboard.setWeeklyRevenue(BigDecimal.ZERO);
        dashboard.setAverageOrderValue(BigDecimal.ZERO);
        dashboard.setFulfillmentRate(0.0);

        return dashboard;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateMonthlyRevenue(UUID storeId, YearMonth yearMonth) {
        // This would require custom queries to sum platform fees or seller ledger entries
        // Placeholder implementation
        return BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public Double calculateFulfillmentRate(UUID storeId) {
        // Calculate percentage of orders shipped/delivered on time
        // Would need custom query with date comparisons
        return 0.0;
    }
}
