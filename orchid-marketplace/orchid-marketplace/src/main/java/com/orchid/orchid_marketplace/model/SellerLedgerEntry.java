package com.orchid.orchid_marketplace.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
@Table(name = "seller_ledger_entries")
public class SellerLedgerEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private SellerLedgerEntryType type;

    // Signed amount: positive credits seller, negative debits seller.
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    // If false, this entry is informational (e.g., tax collected/remitted by marketplace).
    @Column(name = "affects_seller_balance", nullable = false)
    private Boolean affectsSellerBalance = true;

    // Optional references
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "order_item_id")
    private UUID orderItemId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    // Listing fee settlement tracking
    @Column(name = "is_settled", nullable = false)
    private Boolean isSettled = false;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @Column(name = "settled_order_id")
    private UUID settledOrderId;

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public SellerLedgerEntryType getType() { return type; }
    public void setType(SellerLedgerEntryType type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Boolean getAffectsSellerBalance() { return affectsSellerBalance; }
    public void setAffectsSellerBalance(Boolean affectsSellerBalance) { this.affectsSellerBalance = affectsSellerBalance; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getOrderItemId() { return orderItemId; }
    public void setOrderItemId(UUID orderItemId) { this.orderItemId = orderItemId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getStripePaymentIntentId() { return stripePaymentIntentId; }
    public void setStripePaymentIntentId(String stripePaymentIntentId) { this.stripePaymentIntentId = stripePaymentIntentId; }

    public Boolean getIsSettled() { return isSettled; }
    public void setIsSettled(Boolean isSettled) { this.isSettled = isSettled; }

    public LocalDateTime getSettledAt() { return settledAt; }
    public void setSettledAt(LocalDateTime settledAt) { this.settledAt = settledAt; }

    public UUID getSettledOrderId() { return settledOrderId; }
    public void setSettledOrderId(UUID settledOrderId) { this.settledOrderId = settledOrderId; }
}
