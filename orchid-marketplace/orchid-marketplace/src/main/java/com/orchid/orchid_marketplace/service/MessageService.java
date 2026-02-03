package com.orchid.orchid_marketplace.service;

import com.orchid.orchid_marketplace.model.Conversation;
import com.orchid.orchid_marketplace.model.Message;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.ConversationRepository;
import com.orchid.orchid_marketplace.repository.MessageRepository;
import com.orchid.orchid_marketplace.repository.OrderRepository;
import com.orchid.orchid_marketplace.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Profile("!cosmos")
@Transactional
public class MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // Off-platform payment keywords that indicate potential fraud
    private static final Set<String> OFF_PLATFORM_KEYWORDS = new HashSet<>(Arrays.asList(
        "paypal", "venmo", "cashapp", "zelle", "bank", "wire", "transfer",
        "western union", "moneygram", "bitcoin", "crypto", "ethereum",
        "whatsapp", "telegram", "signal", "wechat", "alipay",
        "check", "cashier check", "money order", "gift card"
    ));

    public MessageService(ConversationRepository conversationRepository,
                         MessageRepository messageRepository,
                         OrderRepository orderRepository,
                         UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Starts a new conversation between buyer and seller for an order
     */
    public Conversation startConversation(UUID buyerId, UUID sellerId, UUID orderId) {
        // Verify order exists and buyer is the actual buyer
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (!order.getBuyer().getId().equals(buyerId)) {
            throw new RuntimeException("User is not the buyer of this order");
        }

        // Verify seller is associated with an item in the order
        boolean sellerInOrder = order.getOrderItems().stream()
            .anyMatch(item -> item.getStore().getSeller().getId().equals(sellerId));
        
        if (!sellerInOrder) {
            throw new RuntimeException("Seller is not associated with this order");
        }

        // Check if conversation already exists
        Optional<Conversation> existing = conversationRepository
            .findByBuyerIdAndSellerIdAndOrderId(buyerId, sellerId, orderId);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Create new conversation
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new RuntimeException("Buyer not found: " + buyerId));
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new RuntimeException("Seller not found: " + sellerId));

        Conversation conversation = new Conversation();
        conversation.setBuyer(buyer);
        conversation.setSeller(seller);
        conversation.setOrder(order);
        conversation.setIsArchived(false);
        conversation.updateLastMessageTime();

        return conversationRepository.save(conversation);
    }

    /**
     * Sends a message in a conversation
     * Detects off-platform keywords and flags if found
     */
    public Message sendMessage(UUID conversationId, UUID senderId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));

        // Verify user is a participant in conversation
        if (!conversation.hasParticipant(senderId)) {
            throw new RuntimeException("User is not a participant in this conversation");
        }

        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new RuntimeException("Sender not found: " + senderId));

        // Create message
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content);
        message.setIsRead(false);

        // Detect flagged keywords
        List<String> detectedKeywords = detectOffPlatformKeywords(content);
        if (!detectedKeywords.isEmpty()) {
            message.markWithFlaggedKeywords(String.join(", ", detectedKeywords));
        }

        Message savedMessage = messageRepository.save(message);

        // Update conversation's last message time
        conversation.updateLastMessageTime();
        conversationRepository.save(conversation);

        return savedMessage;
    }

    /**
     * Marks a message as read
     */
    public Message markAsRead(UUID messageId, UUID userId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found: " + messageId));

        // Only the recipient can mark as read
        Conversation conversation = message.getConversation();
        User recipient = conversation.getOtherParticipant(message.getSender().getId());

        if (!recipient.getId().equals(userId)) {
            throw new RuntimeException("User is not the recipient of this message");
        }

        message.markAsRead();
        return messageRepository.save(message);
    }

    /**
     * Gets all messages in a conversation
     */
    public List<Message> getConversationMessages(UUID conversationId, UUID userId) {
        // Verify user has access to this conversation
        conversationRepository.findByIdAndParticipant(conversationId, userId)
            .orElseThrow(() -> new RuntimeException("Conversation not found or access denied"));

        return messageRepository.findByConversationId(conversationId);
    }

    /**
     * Gets all conversations for a user
     */
    public List<Conversation> getUserConversations(UUID userId) {
        return conversationRepository.findUnArchivedByUserId(userId);
    }

    /**
     * Archives a conversation
     */
    public Conversation archiveConversation(UUID conversationId, UUID userId) {
        Conversation conversation = conversationRepository.findByIdAndParticipant(conversationId, userId)
            .orElseThrow(() -> new RuntimeException("Conversation not found or access denied"));

        conversation.setIsArchived(true);
        return conversationRepository.save(conversation);
    }

    /**
     * Unarchives a conversation
     */
    public Conversation unarchiveConversation(UUID conversationId, UUID userId) {
        Conversation conversation = conversationRepository.findByIdAndParticipant(conversationId, userId)
            .orElseThrow(() -> new RuntimeException("Conversation not found or access denied"));

        conversation.setIsArchived(false);
        return conversationRepository.save(conversation);
    }

    /**
     * Gets all conversations with flagged messages (for admin review)
     */
    public List<Conversation> getFlaggedConversations() {
        return conversationRepository.findConversationsWithFlaggedMessages();
    }

    /**
     * Gets all flagged messages in a conversation
     */
    public List<Message> getFlaggedMessagesInConversation(UUID conversationId) {
        return messageRepository.findFlaggedMessagesByConversationId(conversationId);
    }

    /**
     * Counts unread messages for a user
     */
    public long getUnreadMessageCount(UUID userId) {
        return conversationRepository.countUnreadMessagesForUser(userId);
    }

    /**
     * Detects off-platform payment keywords in message content
     * Returns list of detected keywords (case-insensitive)
     */
    private List<String> detectOffPlatformKeywords(String content) {
        if (content == null || content.isEmpty()) {
            return Collections.emptyList();
        }

        String lowerContent = content.toLowerCase();
        return OFF_PLATFORM_KEYWORDS.stream()
            .filter(keyword -> {
                // Create word boundary regex to match whole words only
                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b");
                return pattern.matcher(lowerContent).find();
            })
            .collect(Collectors.toList());
    }
}

