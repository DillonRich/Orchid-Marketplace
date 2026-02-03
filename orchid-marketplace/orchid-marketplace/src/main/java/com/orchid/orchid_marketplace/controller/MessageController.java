package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.orchid.orchid_marketplace.dto.ConversationResponse;
import com.orchid.orchid_marketplace.dto.MessageResponse;
import com.orchid.orchid_marketplace.dto.SendMessageRequest;
import com.orchid.orchid_marketplace.dto.StartConversationRequest;
import com.orchid.orchid_marketplace.mapper.ConversationMapper;
import com.orchid.orchid_marketplace.model.Conversation;
import com.orchid.orchid_marketplace.model.Message;
import com.orchid.orchid_marketplace.service.MessageService;
import com.orchid.orchid_marketplace.service.UserService;

import jakarta.validation.Valid;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final ConversationMapper conversationMapper;
    private final UserService userService;

    public MessageController(MessageService messageService, ConversationMapper conversationMapper, UserService userService) {
        this.messageService = messageService;
        this.conversationMapper = conversationMapper;
        this.userService = userService;
    }

    /**
     * Start a new conversation with a seller
     * POST /api/messages/start
     */
    @PostMapping("/start")
    public ResponseEntity<ConversationResponse> startConversation(
            @Valid @RequestBody StartConversationRequest request,
            Authentication authentication) {
        UUID buyerId = getUserId(authentication);

        Conversation conversation = messageService.startConversation(
            buyerId,
            request.getSellerId(),
            request.getOrderId()
        );

        // Send initial message if provided
        if (request.getInitialMessage() != null && !request.getInitialMessage().isBlank()) {
            messageService.sendMessage(conversation.getId(), buyerId, request.getInitialMessage());
            // Refresh conversation to include initial message
            final UUID conversationId = conversation.getId();
            conversation = messageService.getUserConversations(buyerId).stream()
                .filter(c -> c.getId().equals(conversationId))
                .findFirst()
                .orElse(conversation);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(conversationMapper.toResponse(conversation));
    }

    /**
     * Get all conversations for the current user
     * GET /api/messages/conversations
     */
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(
            Authentication authentication) {
        UUID userId = getUserId(authentication);

        List<Conversation> conversations = messageService.getUserConversations(userId);
        List<ConversationResponse> responses = conversations.stream()
            .map(conversationMapper::toResponseWithoutMessages)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Get a specific conversation with all messages
     * GET /api/messages/conversations/{conversationId}
     */
    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ConversationResponse> getConversation(
            @PathVariable UUID conversationId,
            Authentication authentication) {
        UUID userId = getUserId(authentication);

        List<Message> messages = messageService.getConversationMessages(conversationId, userId);
        
        ConversationResponse response = new ConversationResponse();
        if (!messages.isEmpty()) {
            Conversation conversation = messages.get(0).getConversation();
            response = conversationMapper.toResponse(conversation);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Send a message in a conversation
     * POST /api/messages/conversations/{conversationId}/send
     */
    @PostMapping("/conversations/{conversationId}/send")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable UUID conversationId,
            @Valid @RequestBody SendMessageRequest request,
            Authentication authentication) {
        UUID userId = getUserId(authentication);

        if (!request.getConversationId().equals(conversationId)) {
            return ResponseEntity.badRequest().build();
        }

        Message message = messageService.sendMessage(
            conversationId,
            userId,
            request.getContent()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(conversationMapper.toMessageResponse(message));
    }

    /**
     * Mark a message as read
     * PUT /api/messages/{messageId}/read
     */
    @PutMapping("/{messageId}/read")
    public ResponseEntity<MessageResponse> markMessageAsRead(
            @PathVariable UUID messageId,
            Authentication authentication) {
        UUID userId = getUserId(authentication);

        Message message = messageService.markAsRead(messageId, userId);

        return ResponseEntity.ok(conversationMapper.toMessageResponse(message));
    }

    /**
     * Get unread message count
     * GET /api/messages/unread-count
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            Authentication authentication) {
        UUID userId = getUserId(authentication);

        long unreadCount = messageService.getUnreadMessageCount(userId);

        return ResponseEntity.ok(unreadCount);
    }

    /**
     * Archive a conversation
     * PUT /api/messages/conversations/{conversationId}/archive
     */
    @PutMapping("/conversations/{conversationId}/archive")
    public ResponseEntity<ConversationResponse> archiveConversation(
            @PathVariable UUID conversationId,
            Authentication authentication) {
        UUID userId = getUserId(authentication);

        Conversation conversation = messageService.archiveConversation(conversationId, userId);

        return ResponseEntity.ok(conversationMapper.toResponseWithoutMessages(conversation));
    }

    /**
     * Unarchive a conversation
     * PUT /api/messages/conversations/{conversationId}/unarchive
     */
    @PutMapping("/conversations/{conversationId}/unarchive")
    public ResponseEntity<ConversationResponse> unarchiveConversation(
            @PathVariable UUID conversationId,
            Authentication authentication) {
        UUID userId = getUserId(authentication);

        Conversation conversation = messageService.unarchiveConversation(conversationId, userId);

        return ResponseEntity.ok(conversationMapper.toResponseWithoutMessages(conversation));
    }

    /**
     * Get flagged conversations (ADMIN ONLY)
     * GET /api/messages/admin/flagged
     */
    @GetMapping("/admin/flagged")
    public ResponseEntity<List<ConversationResponse>> getFlaggedConversations() {
        List<Conversation> conversations = messageService.getFlaggedConversations();
        List<ConversationResponse> responses = conversations.stream()
            .map(conversationMapper::toResponseWithoutMessages)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Error response DTO
     */
    public static class ErrorResponse {
        public String message;
        public String detail;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public ErrorResponse(String message, String detail) {
            this.message = message;
            this.detail = detail;
        }
    }

    private UUID getUserId(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName())
            .map(user -> {
                if (user.getId() == null) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User id not found");
                }
                return user.getId();
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}
