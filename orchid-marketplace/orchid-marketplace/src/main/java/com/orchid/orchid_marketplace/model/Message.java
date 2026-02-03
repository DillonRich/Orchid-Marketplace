package com.orchid.orchid_marketplace.model;

import com.orchid.orchid_marketplace.model.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "has_flagged_keywords", nullable = false)
    private Boolean hasFlaggedKeywords = false;

    @Column(name = "flagged_keywords", columnDefinition = "TEXT")
    private String flaggedKeywords; // Comma-separated list of detected keywords

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    // Constructors
    public Message() {
    }

    public Message(Conversation conversation, User sender, String content) {
        this.conversation = conversation;
        this.sender = sender;
        this.content = content;
        this.isRead = false;
        this.hasFlaggedKeywords = false;
    }

    // Getters and Setters
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getHasFlaggedKeywords() {
        return hasFlaggedKeywords;
    }

    public void setHasFlaggedKeywords(Boolean hasFlaggedKeywords) {
        this.hasFlaggedKeywords = hasFlaggedKeywords;
    }

    public String getFlaggedKeywords() {
        return flaggedKeywords;
    }

    public void setFlaggedKeywords(String flaggedKeywords) {
        this.flaggedKeywords = flaggedKeywords;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    /**
     * Marks message as read
     */
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    /**
     * Marks message with flagged keywords
     * @param keywords Comma-separated list of flagged keywords
     */
    public void markWithFlaggedKeywords(String keywords) {
        this.hasFlaggedKeywords = true;
        this.flaggedKeywords = keywords;
    }
}
