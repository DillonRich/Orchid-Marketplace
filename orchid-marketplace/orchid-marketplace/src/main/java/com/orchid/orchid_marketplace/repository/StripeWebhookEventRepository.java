package com.orchid.orchid_marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.StripeWebhookEvent;

@Repository
public interface StripeWebhookEventRepository extends JpaRepository<StripeWebhookEvent, java.util.UUID> {
    boolean existsByEventId(String eventId);
}
