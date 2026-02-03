package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.CheckoutRequest;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.service.CheckoutService;
import com.orchid.orchid_marketplace.service.stripe.StripeCheckoutService;

class CheckoutControllerTest {

    private CheckoutController controller;

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private StripeCheckoutService stripeCheckoutService;

    @Mock
    private Authentication authentication;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new CheckoutController();
        ReflectionTestUtils.setField(controller, "checkoutService", checkoutService);
        ReflectionTestUtils.setField(controller, "stripeCheckoutService", stripeCheckoutService);

        user = new User();
        user.setId(UUID.randomUUID());
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void checkout_returnsOrderResponse() {
        CheckoutRequest req = new CheckoutRequest();
        req.setShippingAddressId(UUID.randomUUID());
        req.setBillingAddressId(UUID.randomUUID());

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("10.00"));
        order.setCreatedAt(LocalDateTime.now());

        when(checkoutService.createOrderFromCart(any(), any(), any())).thenReturn(order);

        ResponseEntity<?> response = controller.checkout(req, authentication);

        assertEquals(200, response.getStatusCode().value());
        Object body = response.getBody();
        assertTrue(body instanceof com.orchid.orchid_marketplace.dto.CheckoutResponse);
        com.orchid.orchid_marketplace.dto.CheckoutResponse resp = (com.orchid.orchid_marketplace.dto.CheckoutResponse) body;
        assertEquals(order.getId(), resp.getOrderId());
        assertEquals("Order created successfully. Proceed to payment.", resp.getMessage());
    }

    @Test
    void getOrder_forbiddenWhenNotOwner() {
        UUID orderId = UUID.randomUUID();
        when(checkoutService.doesOrderBelongToUser(orderId, user.getId())).thenReturn(false);

        ResponseEntity<?> response = controller.getOrder(orderId, authentication);

        assertEquals(403, response.getStatusCode().value());
    }
}
