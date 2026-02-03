package com.orchid.orchid_marketplace.service.stripe;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.config.StripeProperties;
import com.orchid.orchid_marketplace.dto.StripeCheckoutSessionResponse;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.OrderItem;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.OrderItemRepository;
import com.orchid.orchid_marketplace.repository.OrderRepository;
import com.orchid.orchid_marketplace.service.SellerLedgerService;
import com.orchid.orchid_marketplace.util.MoneyUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
@Profile("!cosmos")
public class StripeCheckoutService {

    @Autowired
    private StripeProperties stripeProperties;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SellerLedgerService sellerLedgerService;

    @Autowired
    private StripeIdempotencyService stripeIdempotencyService;

    @Transactional(readOnly = true)
    public StripeCheckoutSessionResponse createCheckoutSession(UUID orderId, String successUrl, String cancelUrl) throws StripeException {
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(successUrl, "successUrl must not be null");
        Objects.requireNonNull(cancelUrl, "cancelUrl must not be null");

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        List<OrderItem> items = orderItemRepository.findActiveByOrder(orderId);
        if (items.isEmpty()) {
            throw new RuntimeException("Order has no active items");
        }

        // v1: enforce single-seller orders for Stripe Checkout destination charges.
        UUID storeId = null;
        for (OrderItem oi : items) {
            if (oi.getStore() == null || oi.getStore().getId() == null) {
                throw new RuntimeException("Order item missing store");
            }
            if (storeId == null) {
                storeId = oi.getStore().getId();
            } else if (!oi.getStore().getId().equals(storeId)) {
                throw new RuntimeException("Multi-seller orders are not supported in v1 checkout");
            }
        }

        if (storeId == null) {
            throw new RuntimeException("Order items missing store");
        }

        Store store = items.get(0).getStore();
        User seller = store.getSeller();
        if (seller == null || seller.getStripeConnectAccountId() == null || seller.getStripeConnectAccountId().isBlank()) {
            throw new RuntimeException("Seller does not have a Stripe Connect account ID on file");
        }

        long itemSubtotalCents = 0;
        for (OrderItem oi : items) {
            long unit = MoneyUtil.dollarsToCents(oi.getUnitPrice());
            Integer qtyObj = oi.getQuantity();
            long qty = qtyObj == null ? 0L : qtyObj.longValue();
            itemSubtotalCents += unit * qty;
        }

        long shippingCents = MoneyUtil.dollarsToCents(order.getShippingAmount());

        long outstandingListingFees = sellerLedgerService.countOutstandingListingFees(storeId) * stripeProperties.getListingFeeCents();
        BigDecimal platformFeePercent = stripeProperties.getPlatformFeePercent();
        StripeFeeBreakdown fees = StripeFeeCalculator.calculate(itemSubtotalCents, shippingCents, platformFeePercent, outstandingListingFees);

        SessionCreateParams.Builder params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
            // Cards + wallet payments (Apple Pay/Google Pay) are available via "card" in Checkout.
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setShippingAddressCollection(
                SessionCreateParams.ShippingAddressCollection.builder()
                    .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                    .build())
            .putMetadata("order_id", orderId.toString())
            .putMetadata("platform_fee_cents", String.valueOf(fees.getPlatformFeeCents()))
            .putMetadata("listing_fee_applied_cents", String.valueOf(fees.getListingFeeAppliedCents()));

        // Buyer email (helps reduce friction).
        if (order.getBuyer() != null && order.getBuyer().getEmail() != null) {
            params.setCustomerEmail(order.getBuyer().getEmail());
        }

        for (OrderItem oi : items) {
            String name = oi.getProduct() != null && oi.getProduct().getTitle() != null ? oi.getProduct().getTitle() : "Item";
            long unit = MoneyUtil.dollarsToCents(oi.getUnitPrice());
            Integer qtyObj = oi.getQuantity();
            long qty = qtyObj == null ? 0L : qtyObj.longValue();

            params.addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(qty)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(stripeProperties.getCurrency())
                            .setUnitAmount(unit)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName(name).build())
                            .build())
                    .build()
            );
        }

        if (shippingCents > 0) {
            params.addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(stripeProperties.getCurrency())
                            .setUnitAmount(shippingCents)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("Shipping").build())
                            .build())
                    .build()
            );
        }

        params.setPaymentIntentData(
            SessionCreateParams.PaymentIntentData.builder()
                .setApplicationFeeAmount(fees.getApplicationFeeCents())
                .setTransferData(
                    SessionCreateParams.PaymentIntentData.TransferData.builder()
                        .setDestination(seller.getStripeConnectAccountId())
                        .build())
                .putMetadata("order_id", orderId.toString())
                .putMetadata("platform_fee_cents", String.valueOf(fees.getPlatformFeeCents()))
                .putMetadata("listing_fee_applied_cents", String.valueOf(fees.getListingFeeAppliedCents()))
                .build()
        );

        String idempotencyKey = stripeIdempotencyService.generateAndStore("checkout_session", orderId.toString());
        
        Session session = Session.create(params.build(), com.stripe.net.RequestOptions.builder()
            .setIdempotencyKey(idempotencyKey)
            .build());

        StripeCheckoutSessionResponse response = new StripeCheckoutSessionResponse();
        response.setSessionId(session.getId());
        response.setUrl(session.getUrl());
        response.setPlatformFeeCents(fees.getPlatformFeeCents());
        response.setListingFeeAppliedCents(fees.getListingFeeAppliedCents());
        return response;
    }
}
