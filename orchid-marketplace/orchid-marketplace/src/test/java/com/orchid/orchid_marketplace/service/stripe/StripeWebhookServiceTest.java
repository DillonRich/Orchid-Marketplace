package com.orchid.orchid_marketplace.service.stripe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orchid.orchid_marketplace.config.StripeProperties;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.StripeWebhookEvent;
import com.orchid.orchid_marketplace.repository.OrderRepository;
import com.orchid.orchid_marketplace.repository.StripeWebhookEventRepository;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;

@ExtendWith(MockitoExtension.class)
class StripeWebhookServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StripeWebhookEventRepository webhookEventRepository;

    private StripeWebhookService stripeWebhookService;

    @BeforeEach
    @SuppressWarnings("unused")
    void beforeEach() {
        StripeProperties props = new StripeProperties();
        props.setWebhookSecret("whsec_test");
        stripeWebhookService = new StripeWebhookService(props, orderRepository, webhookEventRepository);
    }

    @Test
    void handlePaymentIntentSucceeded_updatesOrderAndRecordsEvent() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.OrderStatus.PENDING);

        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setId("pi_123");
        paymentIntent.setLatestCharge("ch_123");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("order_id", orderId.toString());
        paymentIntent.setMetadata(metadata);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        stripeWebhookService.handlePaymentIntentSucceeded("evt_123", "payment_intent.succeeded", paymentIntent);

        assertEquals(Order.OrderStatus.PROCESSING, order.getStatus());
        assertEquals("pi_123", order.getStripePaymentIntentId());
        assertEquals("ch_123", order.getStripeChargeId());
        verify(orderRepository).save(order);
        verify(webhookEventRepository).save(any(StripeWebhookEvent.class));
    }

    @Test
    void handleEvent_skipsWhenAlreadyProcessed() {
        Event event = new Event();
        event.setId("evt_456");
        event.setType("payment_intent.succeeded");

        when(webhookEventRepository.existsByEventId("evt_456")).thenReturn(true);

        stripeWebhookService.handleEvent(event);

        verify(orderRepository, never()).save(any());
        verify(webhookEventRepository, never()).save(any());
    }
}
