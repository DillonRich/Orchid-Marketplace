package com.orchid.orchid_marketplace.model;

import java.time.LocalDateTime;

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
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private NotificationChannel channel;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", length = 2000)
    private String message;

    @Column(name = "subject")
    private String subject; // For email

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "related_order_id")
    private java.util.UUID relatedOrderId;

    @Column(name = "related_product_id")
    private java.util.UUID relatedProductId;

    public enum NotificationType {
        ORDER_CONFIRMED,
        ORDER_SHIPPED,
        ORDER_DELIVERED,
        PAYMENT_RECEIVED,
        PAYMENT_FAILED,
        REVIEW_POSTED,
        SELLER_RESPONSE,
        REFUND_PROCESSED,
        PROMOTIONAL,
        SYSTEM_MESSAGE
    }

    public enum NotificationChannel {
        IN_APP,
        EMAIL,
        SMS,
        PUSH
    }

    // Getters and Setters
    public User getRecipient() { return recipient; }
    public void setRecipient(User recipient) { this.recipient = recipient; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public java.util.UUID getRelatedOrderId() { return relatedOrderId; }
    public void setRelatedOrderId(java.util.UUID relatedOrderId) { this.relatedOrderId = relatedOrderId; }

    public java.util.UUID getRelatedProductId() { return relatedProductId; }
    public void setRelatedProductId(java.util.UUID relatedProductId) { this.relatedProductId = relatedProductId; }
}
