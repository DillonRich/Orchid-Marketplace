package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.ConversationResponse;
import com.orchid.orchid_marketplace.dto.MessageResponse;
import com.orchid.orchid_marketplace.model.Conversation;
import com.orchid.orchid_marketplace.model.Message;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ConversationMapper {

    /**
     * Maps a Conversation entity to ConversationResponse DTO
     */
    public ConversationResponse toResponse(Conversation conversation) {
        if (conversation == null) {
            return null;
        }

        ConversationResponse response = new ConversationResponse();
        response.setId(conversation.getId());
        response.setBuyerId(conversation.getBuyer().getId());
        response.setBuyerName(conversation.getBuyer().getFullName() != null ? 
            conversation.getBuyer().getFullName() : conversation.getBuyer().getEmail());
        response.setBuyerEmail(conversation.getBuyer().getEmail());
        response.setSellerId(conversation.getSeller().getId());
        response.setSellerName(conversation.getSeller().getFullName() != null ? 
            conversation.getSeller().getFullName() : conversation.getSeller().getEmail());
        response.setSellerEmail(conversation.getSeller().getEmail());
        
        if (conversation.getOrder() != null) {
            response.setOrderId(conversation.getOrder().getId());
            response.setOrderNumber(conversation.getOrder().getOrderNumber());
        }

        if (conversation.getMessages() != null) {
            response.setMessageCount(conversation.getMessages().size());
            response.setMessages(
                conversation.getMessages().stream()
                    .map(this::toMessageResponse)
                    .collect(Collectors.toList())
            );
        } else {
            response.setMessageCount(0);
            response.setMessages(new ArrayList<>());
        }

        response.setLastMessageAt(conversation.getLastMessageAt());
        response.setIsArchived(conversation.getIsArchived());
        response.setCreatedAt(conversation.getCreatedAt());
        response.setUpdatedAt(conversation.getUpdatedAt());

        return response;
    }

    /**
     * Maps a Conversation entity to ConversationResponse DTO without messages
     */
    public ConversationResponse toResponseWithoutMessages(Conversation conversation) {
        if (conversation == null) {
            return null;
        }

        ConversationResponse response = new ConversationResponse();
        response.setId(conversation.getId());
        response.setBuyerId(conversation.getBuyer().getId());
        response.setBuyerName(conversation.getBuyer().getFullName() != null ? 
            conversation.getBuyer().getFullName() : conversation.getBuyer().getEmail());
        response.setBuyerEmail(conversation.getBuyer().getEmail());
        response.setSellerId(conversation.getSeller().getId());
        response.setSellerName(conversation.getSeller().getFullName() != null ? 
            conversation.getSeller().getFullName() : conversation.getSeller().getEmail());
        response.setSellerEmail(conversation.getSeller().getEmail());
        
        if (conversation.getOrder() != null) {
            response.setOrderId(conversation.getOrder().getId());
            response.setOrderNumber(conversation.getOrder().getOrderNumber());
        }

        response.setMessageCount(conversation.getMessages() != null ? conversation.getMessages().size() : 0);
        response.setMessages(new ArrayList<>());
        response.setLastMessageAt(conversation.getLastMessageAt());
        response.setIsArchived(conversation.getIsArchived());
        response.setCreatedAt(conversation.getCreatedAt());
        response.setUpdatedAt(conversation.getUpdatedAt());

        return response;
    }

    /**
     * Maps a Message entity to MessageResponse DTO
     */
    public MessageResponse toMessageResponse(Message message) {
        if (message == null) {
            return null;
        }

        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setConversationId(message.getConversation().getId());
        response.setSenderId(message.getSender().getId());
        response.setSenderName(message.getSender().getFullName() != null ? 
            message.getSender().getFullName() : message.getSender().getEmail());
        response.setSenderEmail(message.getSender().getEmail());
        response.setContent(message.getContent());
        response.setHasFlaggedKeywords(message.getHasFlaggedKeywords());
        response.setFlaggedKeywords(message.getFlaggedKeywords());
        response.setIsRead(message.getIsRead());
        response.setReadAt(message.getReadAt());
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        return response;
    }
}
