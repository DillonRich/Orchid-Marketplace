package com.orchid.orchid_marketplace.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.StripeConnectState;

@Repository
public interface StripeConnectStateRepository extends JpaRepository<StripeConnectState, UUID> {
}
