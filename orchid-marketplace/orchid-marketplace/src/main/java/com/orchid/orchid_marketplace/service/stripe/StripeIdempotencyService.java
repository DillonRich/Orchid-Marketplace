package com.orchid.orchid_marketplace.service.stripe;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.model.StripeIdempotencyKey;
import com.orchid.orchid_marketplace.repository.StripeIdempotencyKeyRepository;

@Service
@Profile("!cosmos")
public class StripeIdempotencyService {

    private final StripeIdempotencyKeyRepository idempotencyKeyRepository;

    public StripeIdempotencyService(StripeIdempotencyKeyRepository idempotencyKeyRepository) {
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @Transactional
    public String generateAndStore(String operationType, String resourceId) {
        String key = generateKey();
        
        StripeIdempotencyKey record = new StripeIdempotencyKey();
        record.setIdempotencyKey(key);
        record.setOperationType(operationType);
        record.setResourceId(resourceId);
        idempotencyKeyRepository.save(record);
        
        return key;
    }

    String generateKey() {
        return "idem_" + UUID.randomUUID().toString().replace("-", "");
    }
}
