package com.orchid.orchid_marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class SendMessageRequest {

    @NotNull(message = "Conversation ID is required")
    private UUID conversationId;

    @NotBlank(message = "Message content is required")
    private String content;

    // Constructors
    public SendMessageRequest() {
    }

    public SendMessageRequest(UUID conversationId, String content) {
        this.conversationId = conversationId;
        this.content = content;
    }

    // Getters and Setters
    public UUID getConversationId() { return conversationId; }
    public void setConversationId(UUID conversationId) { this.conversationId = conversationId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

