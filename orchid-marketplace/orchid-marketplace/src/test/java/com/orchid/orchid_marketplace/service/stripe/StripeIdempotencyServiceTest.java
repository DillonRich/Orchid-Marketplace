package com.orchid.orchid_marketplace.service.stripe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orchid.orchid_marketplace.model.StripeIdempotencyKey;
import com.orchid.orchid_marketplace.repository.StripeIdempotencyKeyRepository;

@ExtendWith(MockitoExtension.class)
class StripeIdempotencyServiceTest {

    @Mock
    private StripeIdempotencyKeyRepository idempotencyKeyRepository;

    private StripeIdempotencyService stripeIdempotencyService;

    @BeforeEach
    @SuppressWarnings("unused")
    void beforeEach() {
        stripeIdempotencyService = new StripeIdempotencyService(idempotencyKeyRepository);
    }

    @Test
    void generateKey_createsKeyWithPrefix() {
        String key = stripeIdempotencyService.generateKey();
        
        assertNotNull(key);
        assertTrue(key.startsWith("idem_"));
        assertEquals(37, key.length()); // "idem_" (5) + 32 hex chars (UUID without dashes)
    }

    @Test
    @SuppressWarnings("null")
    void generateAndStore_savesIdempotencyKey() {
        when(idempotencyKeyRepository.save(any(StripeIdempotencyKey.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        String operationType = "checkout_session";
        String resourceId = "order_123";
        
        String key = stripeIdempotencyService.generateAndStore(operationType, resourceId);
        
        assertNotNull(key);
        assertTrue(key.startsWith("idem_"));
        
        ArgumentCaptor<StripeIdempotencyKey> captor = ArgumentCaptor.forClass(StripeIdempotencyKey.class);
        verify(idempotencyKeyRepository).save(captor.capture());
        
        StripeIdempotencyKey saved = captor.getValue();
        assertEquals(key, saved.getIdempotencyKey());
        assertEquals(operationType, saved.getOperationType());
        assertEquals(resourceId, saved.getResourceId());
        assertNotNull(saved.getUsedAt());
    }
}
