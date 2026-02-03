package com.orchid.orchid_marketplace.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.model.Cart;
import com.orchid.orchid_marketplace.model.CartItem;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.ShippingOption;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.CartItemRepository;
import com.orchid.orchid_marketplace.repository.CartRepository;
import com.orchid.orchid_marketplace.repository.ProductRepository;
import com.orchid.orchid_marketplace.repository.ShippingOptionRepository;
import com.orchid.orchid_marketplace.repository.UserRepository;

/**
 * Service for managing shopping cart operations.
 * Handles cart creation, item management, and total calculations.
 */
@Service
@Profile("!cosmos")
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ShippingOptionRepository shippingOptionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.08"); // 8% tax rate
    
    /**
     * Get or create cart for a user.
     */
    @Transactional
    public Cart getOrCreateCart(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
                
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setSubtotal(BigDecimal.ZERO);
                cart.setTax(BigDecimal.ZERO);
                cart.setShippingCost(BigDecimal.ZERO);
                cart.setTotal(BigDecimal.ZERO);
                cart.setLastUpdated(LocalDateTime.now());
                
                return cartRepository.save(cart);
            });
    }
    
    /**
     * Add item to cart or update quantity if already exists.
     */
    @Transactional
    public Cart addToCart(UUID userId, UUID productId, Integer quantity, UUID shippingOptionId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(quantity, "quantity must not be null");
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        // Get product and verify stock
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }
        
        // Get or create cart
        Cart cart = getOrCreateCart(userId);
        
        // Check if product already in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        
        CartItem cartItem;
        if (existingItem.isPresent()) {
            // Update existing item quantity
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock for requested quantity");
            }
            
            cartItem.setQuantity(newQuantity);
        } else {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getItems().add(cartItem);
        }
        
        // Set price at time of adding
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setLineTotal(product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        
        // Set shipping option if provided
        if (shippingOptionId != null) {
            ShippingOption shippingOption = shippingOptionRepository.findById(shippingOptionId)
                .orElseThrow(() -> new RuntimeException("Shipping option not found"));
            
            // Verify shipping option belongs to this product
            if (!shippingOption.getProduct().getId().equals(productId)) {
                throw new RuntimeException("Shipping option does not belong to this product");
            }
            
            cartItem.setShippingOption(shippingOption);
        }
        
        cartItemRepository.save(cartItem);
        
        // Recalculate cart totals
        return recalculateCart(cart);
    }
    
    /**
     * Remove item from cart.
     */
    @Transactional
    public Cart removeFromCart(UUID userId, UUID cartItemId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(cartItemId, "cartItemId must not be null");
        
        Cart cart = getOrCreateCart(userId);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        // Verify item belongs to user's cart
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this cart");
        }
        
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        
        return recalculateCart(cart);
    }
    
    /**
     * Update cart item quantity.
     */
    @Transactional
    public Cart updateCartItemQuantity(UUID userId, UUID cartItemId, Integer newQuantity) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(cartItemId, "cartItemId must not be null");
        Objects.requireNonNull(newQuantity, "newQuantity must not be null");
        
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        
        if (newQuantity == 0) {
            return removeFromCart(userId, cartItemId);
        }
        
        Cart cart = getOrCreateCart(userId);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        // Verify item belongs to user's cart
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this cart");
        }
        
        // Verify stock availability
        if (cartItem.getProduct().getStockQuantity() < newQuantity) {
            throw new RuntimeException("Insufficient stock. Available: " + 
                cartItem.getProduct().getStockQuantity());
        }
        
        cartItem.setQuantity(newQuantity);
        cartItem.setLineTotal(cartItem.getUnitPrice().multiply(new BigDecimal(newQuantity)));
        cartItemRepository.save(cartItem);
        
        return recalculateCart(cart);
    }
    
    /**
     * Update shipping option for a cart item.
     */
    @Transactional
    public Cart updateShippingOption(UUID userId, UUID cartItemId, UUID shippingOptionId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(cartItemId, "cartItemId must not be null");
        
        Cart cart = getOrCreateCart(userId);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        // Verify item belongs to user's cart
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this cart");
        }
        
        if (shippingOptionId != null) {
            ShippingOption shippingOption = shippingOptionRepository.findById(shippingOptionId)
                .orElseThrow(() -> new RuntimeException("Shipping option not found"));
            
            // Verify shipping option belongs to the product
            if (!shippingOption.getProduct().getId().equals(cartItem.getProduct().getId())) {
                throw new RuntimeException("Shipping option does not belong to this product");
            }
            
            cartItem.setShippingOption(shippingOption);
        } else {
            cartItem.setShippingOption(null);
        }
        
        cartItemRepository.save(cartItem);
        
        return recalculateCart(cart);
    }
    
    /**
     * Clear all items from cart.
     */
    @Transactional
    @SuppressWarnings("null")
    public Cart clearCart(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        @SuppressWarnings("null")
        List<CartItem> itemsToDelete = cartItemRepository.findAll().stream()
            .filter(item -> item.getCart().getId().equals(cart.getId()))
            .toList();
        cartItemRepository.deleteAll(itemsToDelete);
        
        return recalculateCart(cart);
    }
    
    /**
     * Get cart for a user.
     */
    public Cart getCart(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return getOrCreateCart(userId);
    }
    
    /**
     * Recalculate cart totals (subtotal, tax, shipping, total).
     */
    @Transactional
    protected Cart recalculateCart(Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");
        
        // Calculate subtotal (sum of all line totals)
        BigDecimal subtotal = cart.getItems().stream()
            .map(CartItem::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate shipping cost (sum of all shipping options)
        BigDecimal shippingCost = cart.getItems().stream()
            .map(item -> item.getShippingOption() != null 
                ? item.getShippingOption().getShippingCost() 
                : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate tax (tax rate applied to subtotal)
        BigDecimal tax = subtotal.multiply(TAX_RATE)
            .setScale(2, RoundingMode.HALF_UP);
        
        // Calculate total
        BigDecimal total = subtotal.add(tax).add(shippingCost);
        
        cart.setSubtotal(subtotal);
        cart.setTax(tax);
        cart.setShippingCost(shippingCost);
        cart.setTotal(total);
        cart.setLastUpdated(LocalDateTime.now());
        
        return cartRepository.save(cart);
    }
    
    /**
     * Validate cart before checkout.
     * Checks stock availability and shipping selections.
     */
    @SuppressWarnings("null")
    public void validateCart(UUID userId) {
        Cart cart = getCart(userId);
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        for (CartItem item : cart.getItems()) {
            // Check stock availability
            @SuppressWarnings("null")
            UUID productId = item.getProduct().getId();
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product no longer available"));
            
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getTitle() + 
                    ". Available: " + product.getStockQuantity());
            }
            
            // Check if shipping option is selected
            if (item.getShippingOption() == null) {
                throw new RuntimeException("Shipping option not selected for " + product.getTitle());
            }
        }
    }
}
