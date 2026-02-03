package com.orchid.orchid_marketplace.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.orchid.orchid_marketplace.model.SellerLedgerEntryType;

public class SellerFinanceEntryResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private SellerLedgerEntryType type;
    private BigDecimal amount;
    private String currency;
    private Boolean affectsSellerBalance;

    private UUID orderId;
    private UUID orderItemId;
    private UUID productId;

    private Boolean isSettled;
    private LocalDateTime settledAt;
    private UUID settledOrderId;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

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

    public Boolean getIsSettled() { return isSettled; }
    public void setIsSettled(Boolean isSettled) { this.isSettled = isSettled; }

    public LocalDateTime getSettledAt() { return settledAt; }
    public void setSettledAt(LocalDateTime settledAt) { this.settledAt = settledAt; }

    public UUID getSettledOrderId() { return settledOrderId; }
    public void setSettledOrderId(UUID settledOrderId) { this.settledOrderId = settledOrderId; }
}
