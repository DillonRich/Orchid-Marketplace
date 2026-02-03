package com.orchid.orchid_marketplace.service.stripe;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.config.StripeProperties;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.StripeWebhookEvent;
import com.orchid.orchid_marketplace.repository.OrderRepository;
import com.orchid.orchid_marketplace.repository.StripeWebhookEventRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

@Service
@Profile("!cosmos")
public class StripeWebhookService {

    private final StripeProperties stripeProperties;
    private final OrderRepository orderRepository;
    private final StripeWebhookEventRepository webhookEventRepository;

    public StripeWebhookService(
        StripeProperties stripeProperties,
        OrderRepository orderRepository,
        StripeWebhookEventRepository webhookEventRepository
    ) {
        this.stripeProperties = stripeProperties;
        this.orderRepository = orderRepository;
        this.webhookEventRepository = webhookEventRepository;
    }

    @Transactional
    public void handleWebhook(String payload, String signatureHeader) {
        Event event = verifyAndParseEvent(payload, signatureHeader);
        handleEvent(event);
    }

    Event verifyAndParseEvent(String payload, String signatureHeader) {
        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("Webhook payload must not be empty");
        }
        if (signatureHeader == null || signatureHeader.isBlank()) {
            throw new IllegalArgumentException("Missing Stripe-Signature header");
        }
        String webhookSecret = stripeProperties.getWebhookSecret();
        if (webhookSecret == null || webhookSecret.isBlank()) {
            throw new IllegalStateException("Stripe webhook secret is not configured (STRIPE_WEBHOOK_SECRET)");
        }
        try {
            return Webhook.constructEvent(payload, signatureHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new IllegalArgumentException("Invalid Stripe webhook signature", e);
        }
    }

    @Transactional
    public void handleEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null");
        }

        String eventId = event.getId();
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("Stripe event missing id");
        }

        if (webhookEventRepository.existsByEventId(eventId)) {
            return; // already processed
        }

        String eventType = event.getType();
        switch (eventType) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = deserialize(event, PaymentIntent.class);
                handlePaymentIntentSucceeded(eventId, eventType, paymentIntent);
                break;
            case "payment_intent.payment_failed":
                PaymentIntent failedPI = deserialize(event, PaymentIntent.class);
                handlePaymentIntentFailed(eventId, eventType, failedPI);
                break;
            case "charge.refunded":
                com.stripe.model.Charge charge = deserialize(event, com.stripe.model.Charge.class);
                handleChargeRefunded(eventId, eventType, charge);
                break;
            case "charge.dispute.created":
                com.stripe.model.Dispute dispute = deserialize(event, com.stripe.model.Dispute.class);
                handleDisputeCreated(eventId, eventType, dispute);
                break;
        }
    }

    void handlePaymentIntentSucceeded(String eventId, String eventType, PaymentIntent paymentIntent) {
        if (paymentIntent == null) {
            throw new IllegalArgumentException("payment_intent payload is required");
        }

        Map<String, String> metadata = paymentIntent.getMetadata();
        String orderIdStr = metadata == null ? null : metadata.get("order_id");
        if (orderIdStr == null || orderIdStr.isBlank()) {
            throw new IllegalStateException("payment_intent missing order_id metadata");
        }

        UUID orderId = UUID.fromString(orderIdStr);
        @SuppressWarnings("null")
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalStateException("Order not found for payment_intent"));

        if (paymentIntent.getId() != null) {
            order.setStripePaymentIntentId(paymentIntent.getId());
        }
        if (paymentIntent.getLatestCharge() != null) {
            order.setStripeChargeId(paymentIntent.getLatestCharge());
        }

        if (order.getStatus() == Order.OrderStatus.PENDING) {
            order.setStatus(Order.OrderStatus.PROCESSING);
        }

        orderRepository.save(order);

        StripeWebhookEvent record = new StripeWebhookEvent();
        record.setEventId(eventId);
        record.setEventType(eventType);
        webhookEventRepository.save(record);
    }

    void handlePaymentIntentFailed(String eventId, String eventType, PaymentIntent paymentIntent) {
        if (paymentIntent == null) {
            throw new IllegalArgumentException("payment_intent payload is required");
        }

        Map<String, String> metadata = paymentIntent.getMetadata();
        String orderIdStr = metadata == null ? null : metadata.get("order_id");
        if (orderIdStr == null || orderIdStr.isBlank()) {
            // Log and skip - not all payment intents have order metadata
            return;
        }

        UUID orderId = UUID.fromString(orderIdStr);
        @SuppressWarnings("null")
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return; // Order not found, skip
        }

        if (order.getStatus() == Order.OrderStatus.PENDING) {
            order.setStatus(Order.OrderStatus.CANCELLED);
        }

        if (paymentIntent.getId() != null) {
            order.setStripePaymentIntentId(paymentIntent.getId());
        }

        orderRepository.save(order);

        StripeWebhookEvent record = new StripeWebhookEvent();
        record.setEventId(eventId);
        record.setEventType(eventType);
        webhookEventRepository.save(record);
    }

    void handleChargeRefunded(String eventId, String eventType, com.stripe.model.Charge charge) {
        if (charge == null) {
            throw new IllegalArgumentException("charge payload is required");
        }

        String paymentIntentId = charge.getPaymentIntent();
        if (paymentIntentId == null || paymentIntentId.isBlank()) {
            return; // No payment intent, skip
        }

        Order order = orderRepository.findByStripePaymentIntentId(paymentIntentId).orElse(null);
        if (order == null) {
            return; // Order not found
        }

        if (charge.getRefunded() != null && charge.getRefunded()) {
            order.setStatus(Order.OrderStatus.REFUNDED);
            orderRepository.save(order);
        }

        StripeWebhookEvent record = new StripeWebhookEvent();
        record.setEventId(eventId);
        record.setEventType(eventType);
        webhookEventRepository.save(record);
    }

    void handleDisputeCreated(String eventId, String eventType, com.stripe.model.Dispute dispute) {
        if (dispute == null) {
            throw new IllegalArgumentException("dispute payload is required");
        }

        String chargeId = dispute.getCharge();
        if (chargeId == null || chargeId.isBlank()) {
            return;
        }

        // Find order by charge ID
        Order order = orderRepository.findByStripeChargeId(chargeId).orElse(null);
        if (order == null) {
            return; // Order not found
        }

        // Mark order status appropriately - could add a DISPUTED status later
        // For now, log the dispute (webhook event record serves as audit trail)

        StripeWebhookEvent record = new StripeWebhookEvent();
        record.setEventId(eventId);
        record.setEventType(eventType);
        webhookEventRepository.save(record);
    }

    private <T extends StripeObject> T deserialize(Event event, Class<T> expectedType) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        Optional<StripeObject> obj = deserializer.getObject();
        if (obj.isEmpty()) {
            throw new IllegalStateException("Unable to deserialize Stripe event payload");
        }
        StripeObject payload = obj.get();
        if (!expectedType.isInstance(payload)) {
            throw new IllegalStateException("Unexpected Stripe payload type: " + payload.getClass().getSimpleName());
        }
        return expectedType.cast(payload);
    }
}
