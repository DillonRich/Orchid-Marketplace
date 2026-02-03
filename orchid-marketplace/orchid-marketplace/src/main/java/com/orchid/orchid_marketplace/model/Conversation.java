package com.orchid.orchid_marketplace.model;

import com.orchid.orchid_marketplace.model.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "conversations")
public class Conversation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @Column(name = "is_archived", nullable = false)
    private Boolean isArchived = false;

    // Constructors
    public Conversation() {
    }

    public Conversation(User buyer, User seller, Order order) {
        this.buyer = buyer;
        this.seller = seller;
        this.order = order;
        this.isArchived = false;
    }

    // Getters and Setters
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    /**
     * Gets the participant on the other end of the conversation
     * @param currentUserId The current user's ID
     * @return The other participant (buyer if current is seller, seller if current is buyer)
     */
    public User getOtherParticipant(UUID currentUserId) {
        if (buyer.getId().equals(currentUserId)) {
            return seller;
        } else if (seller.getId().equals(currentUserId)) {
            return buyer;
        }
        return null; // Shouldn't happen if conversation belongs to user
    }

    /**
     * Checks if a user is part of this conversation
     * @param userId The user ID to check
     * @return true if user is buyer or seller
     */
    public boolean hasParticipant(UUID userId) {
        return buyer.getId().equals(userId) || seller.getId().equals(userId);
    }

    /**
     * Updates last message timestamp
     */
    public void updateLastMessageTime() {
        this.lastMessageAt = LocalDateTime.now();
    }
}

