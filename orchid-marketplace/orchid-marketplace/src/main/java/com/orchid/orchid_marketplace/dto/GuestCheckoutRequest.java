package com.orchid.orchid_marketplace.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for guest checkout without user authentication.
 * Includes email, shipping/billing addresses, and cart items.
 */
public class GuestCheckoutRequest {
    
    @NotBlank(message = "Guest email is required")
    @Email(message = "Valid email is required")
    private String guestEmail;
    
    @NotNull(message = "Shipping address is required")
    private AddressData shippingAddress;
    
    private AddressData billingAddress; // Optional, if null uses shipping address
    
    @NotEmpty(message = "Cart items are required")
    private List<CartItemData> cartItems;
    
    /**
     * Embedded cart item data for guest checkout
     */
    public static class CartItemData {
        @NotBlank(message = "Product ID is required")
        private String productId;
        
        @NotNull(message = "Quantity is required")
        private Integer quantity;
        
        @NotNull(message = "Price is required")
        private Double price;
        
        private String title;
        
        // Getters and Setters
        
        public String getProductId() {
            return productId;
        }
        
        public void setProductId(String productId) {
            this.productId = productId;
        }
        
        public Integer getQuantity() {
            return quantity;
        }
        
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
        
        public Double getPrice() {
            return price;
        }
        
        public void setPrice(Double price) {
            this.price = price;
        }
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
    }
    
    /**
     * Embedded address data for guest checkout
     */
    public static class AddressData {
        @NotBlank(message = "First name is required")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        private String lastName;
        
        @NotBlank(message = "Street address is required")
        private String street;
        
        private String apt;
        
        @NotBlank(message = "City is required")
        private String city;
        
        @NotBlank(message = "State is required")
        private String state;
        
        @NotBlank(message = "ZIP code is required")
        private String zip;
        
        private String phone;
        
        // Getters and Setters
        
        public String getFirstName() {
            return firstName;
        }
        
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
        public String getStreet() {
            return street;
        }
        
        public void setStreet(String street) {
            this.street = street;
        }
        
        public String getApt() {
            return apt;
        }
        
        public void setApt(String apt) {
            this.apt = apt;
        }
        
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        public String getState() {
            return state;
        }
        
        public void setState(String state) {
            this.state = state;
        }
        
        public String getZip() {
            return zip;
        }
        
        public void setZip(String zip) {
            this.zip = zip;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
    
    // Getters and Setters
    
    public String getGuestEmail() {
        return guestEmail;
    }
    
    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }
    
    public AddressData getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(AddressData shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public AddressData getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(AddressData billingAddress) {
        this.billingAddress = billingAddress;
    }
    
    public List<CartItemData> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<CartItemData> cartItems) {
        this.cartItems = cartItems;
    }
}
