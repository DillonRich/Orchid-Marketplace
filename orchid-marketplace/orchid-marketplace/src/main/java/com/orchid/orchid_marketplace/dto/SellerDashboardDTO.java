package com.orchid.orchid_marketplace.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class SellerDashboardDTO {

    private UUID storeId;
    private String storeName;
    
    // Sales metrics
    private Long totalOrders;
    private Long pendingOrders;
    private Long shippedOrders;
    private Long deliveredOrders;
    
    // Revenue metrics
    private BigDecimal totalRevenue;
    private BigDecimal monthlyRevenue;
    private BigDecimal weeklyRevenue;
    
    // Product metrics
    private Long totalProducts;
    private Long activeProducts;
    private Long outOfStockProducts;
    
    // Stripe Connect
    private Boolean stripeConnected;
    private String stripeAccountId;
    
    // Performance
    private BigDecimal averageOrderValue;
    private Double fulfillmentRate; // % of orders shipped on time

    // Getters and Setters
    public UUID getStoreId() { return storeId; }
    public void setStoreId(UUID storeId) { this.storeId = storeId; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public Long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

    public Long getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(Long pendingOrders) { this.pendingOrders = pendingOrders; }

    public Long getShippedOrders() { return shippedOrders; }
    public void setShippedOrders(Long shippedOrders) { this.shippedOrders = shippedOrders; }

    public Long getDeliveredOrders() { return deliveredOrders; }
    public void setDeliveredOrders(Long deliveredOrders) { this.deliveredOrders = deliveredOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getMonthlyRevenue() { return monthlyRevenue; }
    public void setMonthlyRevenue(BigDecimal monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }

    public BigDecimal getWeeklyRevenue() { return weeklyRevenue; }
    public void setWeeklyRevenue(BigDecimal weeklyRevenue) { this.weeklyRevenue = weeklyRevenue; }

    public Long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(Long totalProducts) { this.totalProducts = totalProducts; }

    public Long getActiveProducts() { return activeProducts; }
    public void setActiveProducts(Long activeProducts) { this.activeProducts = activeProducts; }

    public Long getOutOfStockProducts() { return outOfStockProducts; }
    public void setOutOfStockProducts(Long outOfStockProducts) { this.outOfStockProducts = outOfStockProducts; }

    public Boolean getStripeConnected() { return stripeConnected; }
    public void setStripeConnected(Boolean stripeConnected) { this.stripeConnected = stripeConnected; }

    public String getStripeAccountId() { return stripeAccountId; }
    public void setStripeAccountId(String stripeAccountId) { this.stripeAccountId = stripeAccountId; }

    public BigDecimal getAverageOrderValue() { return averageOrderValue; }
    public void setAverageOrderValue(BigDecimal averageOrderValue) { this.averageOrderValue = averageOrderValue; }

    public Double getFulfillmentRate() { return fulfillmentRate; }
    public void setFulfillmentRate(Double fulfillmentRate) { this.fulfillmentRate = fulfillmentRate; }
}
