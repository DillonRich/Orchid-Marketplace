package com.orchid.orchid_marketplace.repository;

import com.orchid.orchid_marketplace.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    /**
     * Find all conversations for a user (as buyer or seller)
     */
    @Query("SELECT c FROM Conversation c WHERE (c.buyer.id = :userId OR c.seller.id = :userId) ORDER BY c.lastMessageAt DESC")
    List<Conversation> findByUserIdOrderByLastMessageDesc(@Param("userId") UUID userId);

    /**
     * Find all unarchived conversations for a user
     */
    @Query("SELECT c FROM Conversation c WHERE (c.buyer.id = :userId OR c.seller.id = :userId) AND c.isArchived = false ORDER BY c.lastMessageAt DESC")
    List<Conversation> findUnArchivedByUserId(@Param("userId") UUID userId);

    /**
     * Find or create conversation between buyer and seller for an order
     */
    Optional<Conversation> findByBuyerIdAndSellerIdAndOrderId(UUID buyerId, UUID sellerId, UUID orderId);

    /**
     * Find conversation by ID and verify user is participant
     */
    @Query("SELECT c FROM Conversation c WHERE c.id = :conversationId AND (c.buyer.id = :userId OR c.seller.id = :userId)")
    Optional<Conversation> findByIdAndParticipant(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);

    /**
     * Find all conversations with flagged messages
     */
    @Query("SELECT DISTINCT c FROM Conversation c JOIN c.messages m WHERE m.hasFlaggedKeywords = true")
    List<Conversation> findConversationsWithFlaggedMessages();

    /**
     * Count unread messages for a user
     */
    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id IN (SELECT c.id FROM Conversation c WHERE (c.buyer.id = :userId OR c.seller.id = :userId)) AND m.isRead = false AND m.sender.id != :userId")
    long countUnreadMessagesForUser(@Param("userId") UUID userId);
}
