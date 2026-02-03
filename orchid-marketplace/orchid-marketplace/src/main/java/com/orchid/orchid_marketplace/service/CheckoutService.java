package com.orchid.orchid_marketplace.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.dto.GuestCheckoutRequest;
import com.orchid.orchid_marketplace.model.Address;
import com.orchid.orchid_marketplace.model.Cart;
import com.orchid.orchid_marketplace.model.CartItem;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.OrderItem;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.AddressRepository;
import com.orchid.orchid_marketplace.repository.OrderItemRepository;
import com.orchid.orchid_marketplace.repository.OrderRepository;
import com.orchid.orchid_marketplace.repository.ProductRepository;
import com.orchid.orchid_marketplace.repository.UserRepository;

/**
 * Service for handling checkout flow.
 * Converts cart to order, reserves stock, and prepares for payment.
 */
@Service
@Profile("!cosmos")
public class CheckoutService {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @SuppressWarnings("unused")
    private SellerLedgerService sellerLedgerService;
    
    /**
     * Create order from cart with stock reservation.
     * Validates addresses, converts cart items to order items, reserves stock.
     */
    @Transactional
    @SuppressWarnings("null")
    public Order createOrderFromCart(UUID userId, UUID shippingAddressId, UUID billingAddressId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(shippingAddressId, "shippingAddressId must not be null");
        Objects.requireNonNull(billingAddressId, "billingAddressId must not be null");
        
        // Validate user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Validate addresses exist and belong to user
        Address shippingAddress = addressRepository.findById(shippingAddressId)
            .orElseThrow(() -> new RuntimeException("Shipping address not found"));
        
        Address billingAddress = addressRepository.findById(billingAddressId)
            .orElseThrow(() -> new RuntimeException("Billing address not found"));
        
        if (!shippingAddress.getUser().getId().equals(userId)) {
            throw new RuntimeException("Shipping address does not belong to user");
        }
        
        if (!billingAddress.getUser().getId().equals(userId)) {
            throw new RuntimeException("Billing address does not belong to user");
        }
        
        // Get and validate cart
        Cart cart = cartService.getCart(userId);
        cartService.validateCart(userId); // Validates stock, shipping options, etc.
        
        // Create order
        Order order = new Order();
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setBuyer(user);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        order.setSubtotal(cart.getSubtotal());
        order.setTaxAmount(cart.getTax());
        order.setShippingAmount(cart.getShippingCost());
        order.setTotalAmount(cart.getTotal());
        
        Order savedOrder = orderRepository.save(order);
        
        // Create order items and reserve stock
        for (CartItem cartItem : cart.getItems()) {
            // Validate stock one more time (race condition check)
            @SuppressWarnings("null")
            UUID productId = cartItem.getProduct().getId();
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product no longer available"));
            
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getTitle());
            }
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setStore(product.getStore());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setItemTotal(cartItem.getLineTotal());
            if (cartItem.getShippingOption() != null) {
                orderItem.setSelectedShippingOption(cartItem.getShippingOption());
                orderItem.setActualShippingCost(cartItem.getShippingOption().getShippingCost());
            }
            orderItem.setStatus(OrderItem.OrderItemStatus.PENDING);
            orderItem.setCreatedAt(LocalDateTime.now());
            
            orderItemRepository.save(orderItem);
            
            // Decrement stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
            
            // Create pending seller ledger entry (will be settled after payment confirmation)
            // NOTE: Seller ledger integration is optional for MVP
            // try {
            //     sellerLedgerService.createPendingLedgerEntry(...);
            // } catch (Exception e) { ... }
        }
        
        // Clear cart after successful order creation
        cartService.clearCart(userId);
        
        return savedOrder;
    }
    
    /**
     * Create order from cart session for guest users (no authentication).
     * Creates temporary Address objects that are not saved to the address repository.
     * Accepts cart items from the frontend instead of session.
     */
    @Transactional
    @SuppressWarnings("null")
    public Order createOrderFromCartGuest(
            String guestEmail,
            GuestCheckoutRequest.AddressData shippingAddressData,
            GuestCheckoutRequest.AddressData billingAddressData,
            java.util.List<GuestCheckoutRequest.CartItemData> cartItems) {
        
        Objects.requireNonNull(guestEmail, "guestEmail must not be null");
        Objects.requireNonNull(shippingAddressData, "shippingAddressData must not be null");
        Objects.requireNonNull(cartItems, "cartItems must not be null");
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Create temporary shipping address (saved for order reference)
        Address shippingAddress = new Address();
        shippingAddress.setRecipientName(shippingAddressData.getFirstName() + " " + shippingAddressData.getLastName());
        shippingAddress.setStreetAddress(shippingAddressData.getStreet() + (shippingAddressData.getApt() != null ? " " + shippingAddressData.getApt() : ""));
        shippingAddress.setCity(shippingAddressData.getCity());
        shippingAddress.setState(shippingAddressData.getState());
        shippingAddress.setPostalCode(shippingAddressData.getZip());
        shippingAddress.setPhoneNumber(shippingAddressData.getPhone());
        shippingAddress.setCountry("US"); // Default country
        shippingAddress.setAddressType(Address.AddressType.SHIPPING);
        
        // Create temporary billing address (use shipping if not provided)
        Address billingAddress = shippingAddress;
        if (billingAddressData != null) {
            billingAddress = new Address();
            billingAddress.setRecipientName(billingAddressData.getFirstName() + " " + billingAddressData.getLastName());
            billingAddress.setStreetAddress(billingAddressData.getStreet() + (billingAddressData.getApt() != null ? " " + billingAddressData.getApt() : ""));
            billingAddress.setCity(billingAddressData.getCity());
            billingAddress.setState(billingAddressData.getState());
            billingAddress.setPostalCode(billingAddressData.getZip());
            billingAddress.setPhoneNumber(billingAddressData.getPhone());
            billingAddress.setCountry("US"); // Default country
            billingAddress.setAddressType(Address.AddressType.BILLING);
        }
        
        // Save addresses
        Address savedShippingAddress = addressRepository.save(shippingAddress);
        Address savedBillingAddress = billingAddressData != null ? addressRepository.save(billingAddress) : savedShippingAddress;
        
        // Calculate totals from cart items
        java.math.BigDecimal subtotal = java.math.BigDecimal.ZERO;
        for (GuestCheckoutRequest.CartItemData item : cartItems) {
            java.math.BigDecimal itemTotal = java.math.BigDecimal.valueOf(item.getPrice())
                .multiply(java.math.BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(itemTotal);
        }
        
        // Calculate tax (8%) and shipping
        java.math.BigDecimal taxAmount = subtotal.multiply(java.math.BigDecimal.valueOf(0.08));
        java.math.BigDecimal shippingAmount = subtotal.compareTo(java.math.BigDecimal.valueOf(50)) >= 0 
            ? java.math.BigDecimal.ZERO 
            : java.math.BigDecimal.valueOf(5.99);
        java.math.BigDecimal totalAmount = subtotal.add(taxAmount).add(shippingAmount);
        
        // Create order with guest email
        Order order = new Order();
        order.setOrderNumber("ORD-GUEST-" + System.currentTimeMillis());
        order.setBuyer(null); // No user for guest checkout
        order.setGuestEmail(guestEmail);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(savedShippingAddress);
        order.setBillingAddress(savedBillingAddress);
        order.setSubtotal(subtotal);
        order.setTaxAmount(taxAmount);
        order.setShippingAmount(shippingAmount);
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        
        // Convert cart items to order items
        for (GuestCheckoutRequest.CartItemData cartItem : cartItems) {
            UUID productId = UUID.fromString(cartItem.getProductId());
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProductId()));
            
            // Check stock availability
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getTitle());
            }
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setStore(product.getStore());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(java.math.BigDecimal.valueOf(cartItem.getPrice()));
            orderItem.setItemTotal(java.math.BigDecimal.valueOf(cartItem.getPrice())
                .multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())));
            orderItem.setStatus(OrderItem.OrderItemStatus.PENDING);
            orderItem.setCreatedAt(LocalDateTime.now());
            
            orderItemRepository.save(orderItem);
            
            // Reserve stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }
        
        return savedOrder;
    }
    
    /**
     * Confirm order payment (called after Stripe webhook confirmation).
     */
    @Transactional
    public Order confirmOrderPayment(UUID orderId) {
        Objects.requireNonNull(orderId, "orderId must not be null");
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot confirm payment for cancelled order");
        }
        
        order.setStatus(Order.OrderStatus.PROCESSING);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        
        // Mark all order items as confirmed
        for (OrderItem item : order.getOrderItems()) {
            item.setStatus(OrderItem.OrderItemStatus.PROCESSING);
            item.setUpdatedAt(LocalDateTime.now());
            orderItemRepository.save(item);
            
            // Settle seller ledger entries for this order item
            // NOTE: Seller ledger integration is optional for MVP
            // try {
            //     sellerLedgerService.settleOrderPayment(...);
            // } catch (Exception e) { ... }
        }
        
        return savedOrder;
    }
    
    /**
     * Cancel order and release reserved stock.
     * Called when payment fails or customer requests cancellation.
     */
    @Transactional    @SuppressWarnings("null")    public Order cancelOrder(UUID orderId, String reason) {
        Objects.requireNonNull(orderId, "orderId must not be null");
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Can only cancel pending or processing orders
        if (order.getStatus() == Order.OrderStatus.SHIPPED || 
            order.getStatus() == Order.OrderStatus.DELIVERED ||
            order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        
        // Restore stock for all items
        for (OrderItem item : order.getOrderItems()) {
            @SuppressWarnings("null")
            UUID productId = item.getProduct().getId();
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
            
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
            
            // Mark order item as cancelled
            item.setStatus(OrderItem.OrderItemStatus.CANCELLED);
            item.setUpdatedAt(LocalDateTime.now());
            orderItemRepository.save(item);
            
            // Reverse seller ledger entries
            // NOTE: Seller ledger integration is optional for MVP
            // try {
            //     sellerLedgerService.reverseLedgerEntry(...);
            // } catch (Exception e) { ... }
        }
        
        return savedOrder;
    }
    
    /**
     * Get order details by order ID.
     */
    @SuppressWarnings("null")
    public Order getOrder(UUID orderId) {
        @SuppressWarnings("null")
        UUID id = orderId;
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    /**
     * Verify order belongs to user.
     */
    @SuppressWarnings("null")
    public boolean doesOrderBelongToUser(UUID orderId, UUID userId) {
        @SuppressWarnings("null")
        UUID id = orderId;
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return false;
        return order.getBuyer().getId().equals(userId);
    }
}
