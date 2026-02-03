package com.orchid.orchid_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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
@SuppressWarnings("null")
class CartServiceTest {
    
    @Mock
    private CartRepository cartRepository;
    
    @Mock
    private CartItemRepository cartItemRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private ShippingOptionRepository shippingOptionRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private CartService cartService;
    
    private User user;
    private Cart cart;
    private Product product;
    private CartItem cartItem;
    private ShippingOption shippingOption;
    private UUID userId;
    private UUID cartId;
    private UUID productId;
    private UUID cartItemId;
    private UUID shippingOptionId;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        cartId = UUID.randomUUID();
        productId = UUID.randomUUID();
        cartItemId = UUID.randomUUID();
        shippingOptionId = UUID.randomUUID();
        
        user = new User();
        user.setId(userId);
        user.setEmail("user@test.com");
        user.setFullName("Test User");
        user.setRole(Role.CUSTOMER);
        
        product = new Product();
        product.setId(productId);
        product.setTitle("Test Product");
        product.setPrice(new BigDecimal("99.99"));
        product.setStockQuantity(10);
        
        shippingOption = new ShippingOption();
        shippingOption.setId(shippingOptionId);
        shippingOption.setProduct(product);
            shippingOption.setOptionName("Standard");
            shippingOption.setShippingCost(new BigDecimal("5.00"));
        
        cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(new BigDecimal("99.99"));
        cartItem.setLineTotal(new BigDecimal("199.98"));
        cartItem.setShippingOption(shippingOption);
        
        cart = new Cart();
        cart.setId(cartId);
        cart.setUser(user);
        cart.setSubtotal(new BigDecimal("199.98"));
        cart.setTax(new BigDecimal("16.00"));
        cart.setShippingCost(new BigDecimal("5.00"));
        cart.setTotal(new BigDecimal("220.98"));
        cart.setItems(new ArrayList<>());
        cart.getItems().add(cartItem);
        cartItem.setCart(cart);
    }
    
    @Test
    void testGetOrCreateCart_ExistingCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        
        Cart result = cartService.getOrCreateCart(userId);
        
        assertNotNull(result);
        assertEquals(cartId, result.getId());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, never()).save(any(Cart.class));
    }
    
    @Test
    void testGetOrCreateCart_NewCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        Cart result = cartService.getOrCreateCart(userId);
        
        assertNotNull(result);
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
    
    @Test
    void testGetOrCreateCart_UserNotFound() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> cartService.getOrCreateCart(userId));
    }
    
    @Test
    void testGetOrCreateCart_NullUserIdThrowsException() {
        assertThrows(NullPointerException.class, () -> cartService.getOrCreateCart(null));
    }
    
    @Test
    void testAddToCart_NewItem() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        Cart result = cartService.addToCart(userId, productId, 2, null);
        
        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }
    
    @Test
    void testAddToCart_UpdateExistingItem() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        Cart result = cartService.addToCart(userId, productId, 1, null);
        
        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }
    
    @Test
    void testAddToCart_WithShippingOption() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.empty());
        when(shippingOptionRepository.findById(shippingOptionId)).thenReturn(Optional.of(shippingOption));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        Cart result = cartService.addToCart(userId, productId, 2, shippingOptionId);
        
        assertNotNull(result);
        verify(shippingOptionRepository, times(1)).findById(shippingOptionId);
    }
    
    @Test
    void testAddToCart_ProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> cartService.addToCart(userId, productId, 2, null));
    }
    
    @Test
    void testAddToCart_InsufficientStock() {
        product.setStockQuantity(1);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        assertThrows(RuntimeException.class, () -> cartService.addToCart(userId, productId, 2, null));
    }
    
    @Test
    void testAddToCart_ZeroQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(userId, productId, 0, null));
    }
    
    @Test
    void testAddToCart_NegativeQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(userId, productId, -1, null));
    }
    
    @Test
    void testAddToCart_NullUserIdThrowsException() {
        assertThrows(NullPointerException.class, () -> cartService.addToCart(null, productId, 2, null));
    }
    
    @Test
    void testAddToCart_NullProductIdThrowsException() {
        assertThrows(NullPointerException.class, () -> cartService.addToCart(userId, null, 2, null));
    }
    
    @Test
    void testAddToCart_NullQuantityThrowsException() {
        assertThrows(NullPointerException.class, () -> cartService.addToCart(userId, productId, null, null));
    }
    
    @Test
    void testAddToCart_ShippingOptionNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.empty());
        when(shippingOptionRepository.findById(shippingOptionId)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> cartService.addToCart(userId, productId, 2, shippingOptionId));
    }
    
    @Test
    void testAddToCart_ShippingOptionDoesNotBelongToProduct() {
        Product otherProduct = new Product();
        otherProduct.setId(UUID.randomUUID());
        shippingOption.setProduct(otherProduct);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.empty());
        when(shippingOptionRepository.findById(shippingOptionId)).thenReturn(Optional.of(shippingOption));
        
        assertThrows(RuntimeException.class, () -> cartService.addToCart(userId, productId, 2, shippingOptionId));
    }
    
    @Test
    void testRemoveFromCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).delete(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        Cart result = cartService.removeFromCart(userId, cartItemId);
        
        assertNotNull(result);
        verify(cartItemRepository, times(1)).delete(cartItem);
    }
    
    @Test
    void testRemoveFromCart_NullUserIdThrowsException() {
        assertThrows(NullPointerException.class, () -> cartService.removeFromCart(null, cartItemId));
    }
    
    @Test
    void testRemoveFromCart_NullCartItemIdThrowsException() {
        assertThrows(NullPointerException.class, () -> cartService.removeFromCart(userId, null));
    }
}
