package com.orchid.orchid_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orchid.orchid_marketplace.model.*;
import com.orchid.orchid_marketplace.repository.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {
    
    @Mock
    private CartService cartService;
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private OrderItemRepository orderItemRepository;
    
    @Mock
    private AddressRepository addressRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private SellerLedgerService sellerLedgerService;
    
    @InjectMocks
    private CheckoutService checkoutService;
    
    private User user;
    private Cart cart;
    private Address shippingAddress;
    private Address billingAddress;
    private Product product;
    private CartItem cartItem;
    private Store store;
    private UUID userId;
    private UUID shippingAddressId;
    private UUID billingAddressId;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        shippingAddressId = UUID.randomUUID();
        billingAddressId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        
        // Setup user
        user = new User();
        user.setId(userId);
        user.setEmail("buyer@test.com");
        user.setFullName("Test Buyer");
        user.setRole(Role.CUSTOMER);
        
        // Setup addresses
        shippingAddress = new Address();
        shippingAddress.setId(shippingAddressId);
        shippingAddress.setUser(user);
        shippingAddress.setStreetAddress("123 Main St");
        shippingAddress.setCity("Test City");
        shippingAddress.setState("TS");
        shippingAddress.setPostalCode("12345");
        
        billingAddress = new Address();
        billingAddress.setId(billingAddressId);
        billingAddress.setUser(user);
        billingAddress.setStreetAddress("123 Main St");
        billingAddress.setCity("Test City");
        billingAddress.setState("TS");
        billingAddress.setPostalCode("12345");
        
        // Setup store
        store = new Store();
        store.setId(storeId);
        store.setStoreName("Test Store");
        
        // Setup product
        product = new Product();
        product.setId(productId);
        product.setTitle("Test Product");
        product.setPrice(new BigDecimal("99.99"));
        product.setStockQuantity(10);
        product.setStore(store);
        
        // Setup cart item
        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(new BigDecimal("99.99"));
        cartItem.setLineTotal(new BigDecimal("199.98"));
        
        // Setup cart
        cart = new Cart();
        cart.setUser(user);
        cart.setSubtotal(new BigDecimal("199.98"));
        cart.setTax(new BigDecimal("16.00"));
        cart.setShippingCost(new BigDecimal("10.00"));
        cart.setTotal(new BigDecimal("225.98"));
        cart.setItems(List.of(cartItem));
    }
    
    @Test
    void testCreateOrderFromCart_Success() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(shippingAddressId)).thenReturn(Optional.of(shippingAddress));
        when(addressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));
        when(cartService.getCart(userId)).thenReturn(cart);
        doNothing().when(cartService).validateCart(userId);
        
        Order mockOrder = new Order("ORD-123", user);
        mockOrder.setId(UUID.randomUUID());
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
        
        // Act
        Order result = checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId);
        
        // Assert
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(addressRepository).findById(shippingAddressId);
        verify(addressRepository).findById(billingAddressId);
        verify(cartService).getCart(userId);
        verify(cartService).validateCart(userId);
        verify(orderRepository).save(any(Order.class));
    }
    
    @Test
    void testCreateOrderFromCart_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, 
            () -> checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_ShippingAddressNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(shippingAddressId)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, 
            () -> checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_BillingAddressNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(shippingAddressId)).thenReturn(Optional.of(shippingAddress));
        when(addressRepository.findById(billingAddressId)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, 
            () -> checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_ShippingAddressDoesNotBelongToUser() {
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        shippingAddress.setUser(otherUser);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(shippingAddressId)).thenReturn(Optional.of(shippingAddress));
        
        assertThrows(RuntimeException.class, 
            () -> checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_BillingAddressDoesNotBelongToUser() {
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        billingAddress.setUser(otherUser);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(shippingAddressId)).thenReturn(Optional.of(shippingAddress));
        when(addressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));
        
        assertThrows(RuntimeException.class, 
            () -> checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_InsufficientStock() {
        product.setStockQuantity(1); // Less than cart quantity of 2
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(shippingAddressId)).thenReturn(Optional.of(shippingAddress));
        when(addressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));
        when(cartService.getCart(userId)).thenReturn(cart);
        doNothing().when(cartService).validateCart(userId);
        
        Order mockOrder = new Order("ORD-123", user);
        mockOrder.setId(UUID.randomUUID());
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        
        assertThrows(RuntimeException.class, 
            () -> checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_ProductNoLongerAvailable() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(shippingAddressId)).thenReturn(Optional.of(shippingAddress));
        when(addressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));
        when(cartService.getCart(userId)).thenReturn(cart);
        doNothing().when(cartService).validateCart(userId);
        
        Order mockOrder = new Order("ORD-123", user);
        mockOrder.setId(UUID.randomUUID());
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, 
            () -> checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_NullUserIdThrowsException() {
        assertThrows(NullPointerException.class, 
            () -> checkoutService.createOrderFromCart(null, shippingAddressId, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_NullShippingAddressIdThrowsException() {
        assertThrows(NullPointerException.class, 
            () -> checkoutService.createOrderFromCart(userId, null, billingAddressId));
    }
    
    @Test
    void testCreateOrderFromCart_NullBillingAddressIdThrowsException() {
        assertThrows(NullPointerException.class, 
            () -> checkoutService.createOrderFromCart(userId, shippingAddressId, null));
    }
    
    @Test
    void testCreateOrderFromCart_OrderTotalsMatchCart() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(shippingAddressId)).thenReturn(Optional.of(shippingAddress));
        when(addressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));
        when(cartService.getCart(userId)).thenReturn(cart);
        doNothing().when(cartService).validateCart(userId);
        
        Order mockOrder = new Order("ORD-123", user);
        mockOrder.setId(UUID.randomUUID());
        
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            assertEquals(cart.getSubtotal(), savedOrder.getSubtotal());
            assertEquals(cart.getTax(), savedOrder.getTaxAmount());
            assertEquals(cart.getShippingCost(), savedOrder.getShippingAmount());
            assertEquals(cart.getTotal(), savedOrder.getTotalAmount());
            return mockOrder;
        });
        
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
        
        checkoutService.createOrderFromCart(userId, shippingAddressId, billingAddressId);
        
        verify(orderRepository).save(any(Order.class));
    }
}
