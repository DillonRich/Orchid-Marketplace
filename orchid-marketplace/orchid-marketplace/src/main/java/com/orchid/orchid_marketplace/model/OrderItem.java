package com.orchid.orchid_marketplace.model;

import java.math.BigDecimal;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    // Shipping fields
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_shipping_option_id")
    private ShippingOption selectedShippingOption;
    
    @Column(name = "actual_shipping_cost")
    private BigDecimal actualShippingCost;
    
    private Integer quantity;
    
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    
    @Column(name = "item_total")
    private BigDecimal itemTotal;
    
    @Column(name = "seller_payout")
    private BigDecimal sellerPayout;
    
    @Enumerated(EnumType.STRING)
    private OrderItemStatus status = OrderItemStatus.PENDING;
    
    public enum OrderItemStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
    
    // Stripe Connect for Payouts
    @Column(name = "stripe_transfer_id")
    private String stripeTransferId;
    
    @Column(name = "stripe_payout_id")
    private String stripePayoutId;
    
    // Azure Service Bus for Async Processing
    @Column(name = "processing_queue_id")
    private String processingQueueId;
    
    // ========== Constructors ==========
    
    public OrderItem() {}
    
    public OrderItem(Order order, Product product, Store store, Integer quantity, BigDecimal unitPrice) {
        this.order = order;
        this.product = product;
        this.store = store;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateItemTotal();
    }
    
    // ========== Getters and Setters ==========
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    
    // Shipping getters and setters
    public ShippingOption getSelectedShippingOption() { return selectedShippingOption; }
    public void setSelectedShippingOption(ShippingOption selectedShippingOption) { 
        this.selectedShippingOption = selectedShippingOption; 
    }
    
    public BigDecimal getActualShippingCost() { return actualShippingCost; }
    public void setActualShippingCost(BigDecimal actualShippingCost) { 
        this.actualShippingCost = actualShippingCost; 
    }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity;
        calculateItemTotal();
    }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { 
        this.unitPrice = unitPrice;
        calculateItemTotal();
    }
    
    public BigDecimal getItemTotal() { return itemTotal; }
    public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
    
    public BigDecimal getSellerPayout() { return sellerPayout; }
    public void setSellerPayout(BigDecimal sellerPayout) { this.sellerPayout = sellerPayout; }
    
    public OrderItemStatus getStatus() { return status; }
    public void setStatus(OrderItemStatus status) { this.status = status; }
    
    public String getStripeTransferId() { return stripeTransferId; }
    public void setStripeTransferId(String stripeTransferId) { this.stripeTransferId = stripeTransferId; }
    
    public String getStripePayoutId() { return stripePayoutId; }
    public void setStripePayoutId(String stripePayoutId) { this.stripePayoutId = stripePayoutId; }
    
    public String getProcessingQueueId() { return processingQueueId; }
    public void setProcessingQueueId(String processingQueueId) { this.processingQueueId = processingQueueId; }
    
    // ========== Helper Methods ==========
    
    private void calculateItemTotal() {
        if (unitPrice != null && quantity != null && quantity > 0) {
            this.itemTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.itemTotal = BigDecimal.ZERO;
        }
    }
    
    public void calculateSellerPayout(BigDecimal platformFeePercentage) {
        if (itemTotal != null && platformFeePercentage != null) {
            // Calculate platform fee
            BigDecimal platformFeeAmount = itemTotal.multiply(
                platformFeePercentage.divide(BigDecimal.valueOf(100)));
            // Seller gets item total minus platform fee
            this.sellerPayout = itemTotal.subtract(platformFeeAmount);
        } else {
            this.sellerPayout = itemTotal;
        }
    }
    
    public BigDecimal calculateTotalWithShipping() {
        BigDecimal total = itemTotal != null ? itemTotal : BigDecimal.ZERO;
        BigDecimal shipping = actualShippingCost != null ? actualShippingCost : BigDecimal.ZERO;
        return total.add(shipping);
    }
    
    public boolean canBeShipped() {
        return status == OrderItemStatus.PROCESSING && selectedShippingOption != null;
    }
    
    public boolean canBeCancelled() {
        return status == OrderItemStatus.PENDING || status == OrderItemStatus.PROCESSING;
    }
    
    public boolean hasShippingSelected() {
        return selectedShippingOption != null;
    }
}