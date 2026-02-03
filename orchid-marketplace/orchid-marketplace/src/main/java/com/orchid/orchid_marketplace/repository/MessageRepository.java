package com.orchid.orchid_marketplace.repository;

import com.orchid.orchid_marketplace.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Find all messages in a conversation, ordered by creation time
     */
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt ASC")
    List<Message> findByConversationId(@Param("conversationId") UUID conversationId);

    /**
     * Find paginated messages in a conversation (for infinite scroll)
     */
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt DESC")
    Page<Message> findByConversationIdPaginated(@Param("conversationId") UUID conversationId, Pageable pageable);

    /**
     * Find all unread messages for a user in a conversation
     */
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId AND m.isRead = false AND m.sender.id != :userId")
    List<Message> findUnreadMessagesInConversation(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);

    /**
     * Find all messages with flagged keywords
     */
    @Query("SELECT m FROM Message m WHERE m.hasFlaggedKeywords = true ORDER BY m.createdAt DESC")
    List<Message> findFlaggedMessages();

    /**
     * Find all messages in a conversation with flagged keywords
     */
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId AND m.hasFlaggedKeywords = true ORDER BY m.createdAt DESC")
    List<Message> findFlaggedMessagesByConversationId(@Param("conversationId") UUID conversationId);

    /**
     * Count total messages in a conversation
     */
    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :conversationId")
    long countByConversationId(@Param("conversationId") UUID conversationId);

    /**
     * Find latest message in a conversation
     */
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt DESC LIMIT 1")
    List<Message> findLatestMessage(@Param("conversationId") UUID conversationId);
}
