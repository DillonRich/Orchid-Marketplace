package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.OrderRequest;
import com.orchid.orchid_marketplace.dto.OrderResponse;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.service.OrderService;

class OrderControllerTest {

    private OrderController controller;

    @Mock
    private OrderService orderService;

    private UUID orderId;
    private UUID buyerId;
    private Order testOrder;
    private User testBuyer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new OrderController();
        ReflectionTestUtils.setField(controller, "orderService", orderService);

        orderId = UUID.randomUUID();
        buyerId = UUID.randomUUID();

        testBuyer = new User();
        testBuyer.setId(buyerId);

        testOrder = new Order();
        testOrder.setId(orderId);
        testOrder.setOrderNumber("ORD-001");
        testOrder.setBuyer(testBuyer);
        testOrder.setTotalAmount(BigDecimal.valueOf(99.99));
        testOrder.setStatus(Order.OrderStatus.PENDING);
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setOrderNumber("ORD-001");

        when(orderService.getAllOrders()).thenReturn(List.of(order1, testOrder));

        List<OrderResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(orderService.getAllOrders()).thenReturn(List.of());

        List<OrderResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_VerifyServiceCalled() {
        when(orderService.getAllOrders()).thenReturn(List.of(testOrder));

        controller.listAll();

        verify(orderService).getAllOrders();
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(testOrder));

        ResponseEntity<OrderResponse> result = controller.getById(orderId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(orderService.getOrderById(orderId)).thenReturn(Optional.empty());

        ResponseEntity<OrderResponse> result = controller.getById(orderId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(testOrder));

        controller.getById(orderId);

        verify(orderService).getOrderById(orderId);
    }

    // ========== byBuyer Tests ==========

    @Test
    void testByBuyer_Success() {
        when(orderService.getOrdersByBuyerId(buyerId)).thenReturn(List.of(testOrder));

        List<Order> result = controller.byBuyer(buyerId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testByBuyer_Empty() {
        when(orderService.getOrdersByBuyerId(buyerId)).thenReturn(List.of());

        List<Order> result = controller.byBuyer(buyerId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testByBuyer_MultipleBuyerOrders() {
        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setOrderNumber("ORD-002");
        order2.setBuyer(testBuyer);

        when(orderService.getOrdersByBuyerId(buyerId)).thenReturn(List.of(testOrder, order2));

        List<Order> result = controller.byBuyer(buyerId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testByBuyer_VerifyServiceCalled() {
        when(orderService.getOrdersByBuyerId(buyerId)).thenReturn(List.of(testOrder));

        controller.byBuyer(buyerId);

        verify(orderService).getOrdersByBuyerId(buyerId);
    }

    // ========== byOrderNumber Tests ==========

    @Test
    void testByOrderNumber_Success() {
        when(orderService.getOrderByOrderNumber("ORD-001")).thenReturn(Optional.of(testOrder));

        ResponseEntity<Order> result = controller.byOrderNumber("ORD-001");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testByOrderNumber_NotFound() {
        when(orderService.getOrderByOrderNumber("NONEXISTENT")).thenReturn(Optional.empty());

        ResponseEntity<Order> result = controller.byOrderNumber("NONEXISTENT");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testByOrderNumber_VerifyServiceCalled() {
        when(orderService.getOrderByOrderNumber("ORD-001")).thenReturn(Optional.of(testOrder));

        controller.byOrderNumber("ORD-001");

        verify(orderService).getOrderByOrderNumber("ORD-001");
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        OrderRequest request = new OrderRequest();

        when(orderService.createOrder(any(Order.class))).thenReturn(testOrder);

        OrderResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        OrderRequest request = new OrderRequest();

        when(orderService.createOrder(any(Order.class))).thenReturn(testOrder);

        controller.create(request);

        verify(orderService).createOrder(any(Order.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        OrderRequest request = new OrderRequest();

        when(orderService.updateOrder(eq(orderId), any(Order.class))).thenReturn(testOrder);

        OrderResponse result = controller.update(orderId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        OrderRequest request = new OrderRequest();

        when(orderService.updateOrder(eq(orderId), any(Order.class))).thenReturn(testOrder);

        controller.update(orderId, request);

        verify(orderService).updateOrder(eq(orderId), any(Order.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(orderService).deleteOrder(orderId);

        ResponseEntity<Void> result = controller.delete(orderId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(orderService).deleteOrder(orderId);

        controller.delete(orderId);

        verify(orderService).deleteOrder(orderId);
    }
}
