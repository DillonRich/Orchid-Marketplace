package com.orchid.orchid_marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class MessageResponse {

    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String senderName;
    private String senderEmail;
    private String content;
    @JsonProperty("hasFlaggedKeywords")
    private Boolean hasFlaggedKeywords;
    private String flaggedKeywords;
    @JsonProperty("isRead")
    private Boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public MessageResponse() {
    }

    public MessageResponse(UUID id, UUID conversationId, UUID senderId, String senderName, String senderEmail, String content, Boolean hasFlaggedKeywords, String flaggedKeywords, Boolean isRead, LocalDateTime readAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.content = content;
        this.hasFlaggedKeywords = hasFlaggedKeywords;
        this.flaggedKeywords = flaggedKeywords;
        this.isRead = isRead;
        this.readAt = readAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getConversationId() { return conversationId; }
    public void setConversationId(UUID conversationId) { this.conversationId = conversationId; }

    public UUID getSenderId() { return senderId; }
    public void setSenderId(UUID senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Boolean getHasFlaggedKeywords() { return hasFlaggedKeywords; }
    public void setHasFlaggedKeywords(Boolean hasFlaggedKeywords) { this.hasFlaggedKeywords = hasFlaggedKeywords; }

    public String getFlaggedKeywords() { return flaggedKeywords; }
    public void setFlaggedKeywords(String flaggedKeywords) { this.flaggedKeywords = flaggedKeywords; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

