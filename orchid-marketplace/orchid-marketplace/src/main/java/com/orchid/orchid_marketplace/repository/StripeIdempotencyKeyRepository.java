package com.orchid.orchid_marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.StripeIdempotencyKey;

@Repository
public interface StripeIdempotencyKeyRepository extends JpaRepository<StripeIdempotencyKey, java.util.UUID> {
    Optional<StripeIdempotencyKey> findByIdempotencyKey(String idempotencyKey);
}
