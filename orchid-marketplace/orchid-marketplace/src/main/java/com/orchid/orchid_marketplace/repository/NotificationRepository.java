package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
    List<Notification> findByRecipientIdAndIsReadFalse(UUID recipientId);
    
    Page<Notification> findByRecipientId(UUID recipientId, Pageable pageable);
    
    long countByRecipientIdAndIsReadFalse(UUID recipientId);
}
