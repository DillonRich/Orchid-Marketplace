package com.orchid.orchid_marketplace.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = true)
    private User buyer;
    
    @Column(name = "guest_email")
    private String guestEmail;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    
    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED
    }
    
    // Stripe Integration
    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;
    
    @Column(name = "stripe_charge_id")
    private String stripeChargeId;
    
    // Azure Service Bus Integration
    @Column(name = "service_bus_message_id")
    private String serviceBusMessageId;
    
    @Column(name = "queue_name")
    private String queueName = "order-processing";
    
    // Financial Fields
    private BigDecimal subtotal;
    
    @Column(name = "tax_amount")
    private BigDecimal taxAmount;
    
    @Column(name = "shipping_amount")
    private BigDecimal shippingAmount;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    @Column(name = "platform_fee")
    private BigDecimal platformFee;
    
    // Relationships
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;
    
    // Twilio Integration
    @Column(name = "sms_notification_id")
    private String smsNotificationId;
    
    @Column(name = "email_notification_id")
    private String emailNotificationId;
    
    // ========== Constructors ==========
    
    public Order() {}
    
    public Order(String orderNumber, User buyer) {
        this.orderNumber = orderNumber;
        this.buyer = buyer;
        this.status = OrderStatus.PENDING;
        this.subtotal = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.shippingAmount = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.platformFee = BigDecimal.ZERO;
    }
    
    // ========== Getters and Setters ==========
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public User getBuyer() {
        return buyer;
    }
    
    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }
    
    public String getGuestEmail() {
        return guestEmail;
    }
    
    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }
    
    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
    
    public String getStripeChargeId() {
        return stripeChargeId;
    }
    
    public void setStripeChargeId(String stripeChargeId) {
        this.stripeChargeId = stripeChargeId;
    }
    
    public String getServiceBusMessageId() {
        return serviceBusMessageId;
    }
    
    public void setServiceBusMessageId(String serviceBusMessageId) {
        this.serviceBusMessageId = serviceBusMessageId;
    }
    
    public String getQueueName() {
        return queueName;
    }
    
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }
    
    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getPlatformFee() {
        return platformFee;
    }
    
    public void setPlatformFee(BigDecimal platformFee) {
        this.platformFee = platformFee;
    }
    
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    public Address getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public Address getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }
    
    public String getSmsNotificationId() {
        return smsNotificationId;
    }
    
    public void setSmsNotificationId(String smsNotificationId) {
        this.smsNotificationId = smsNotificationId;
    }
    
    public String getEmailNotificationId() {
        return emailNotificationId;
    }
    
    public void setEmailNotificationId(String emailNotificationId) {
        this.emailNotificationId = emailNotificationId;
    }
    
    // Helper methods
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        recalculateTotals();
    }
    
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
        recalculateTotals();
    }
    
    public void recalculateTotals() {
        // Calculate subtotal from order items
        BigDecimal itemSubtotal = BigDecimal.ZERO;
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                if (item.getItemTotal() != null) {
                    itemSubtotal = itemSubtotal.add(item.getItemTotal());
                }
            }
        }
        this.subtotal = itemSubtotal;
        
        // Calculate total (subtotal + tax + shipping)
        BigDecimal tax = this.taxAmount != null ? this.taxAmount : BigDecimal.ZERO;
        BigDecimal shipping = this.shippingAmount != null ? this.shippingAmount : BigDecimal.ZERO;
        this.totalAmount = itemSubtotal.add(tax).add(shipping);
    }
    
    public boolean isCancellable() {
        return status == OrderStatus.PENDING || status == OrderStatus.PROCESSING;
    }
    
    public boolean isRefundable() {
        return status == OrderStatus.DELIVERED || status == OrderStatus.SHIPPED;
    }
    
    public int getTotalItems() {
        if (orderItems == null) {
            return 0;
        }
        return orderItems.stream()
            .mapToInt(OrderItem::getQuantity)
            .sum();
    }
}