package com.orchid.orchid_marketplace.mapper;

import java.util.stream.Collectors;

import com.orchid.orchid_marketplace.dto.CartItemResponse;
import com.orchid.orchid_marketplace.dto.CartResponse;
import com.orchid.orchid_marketplace.model.Cart;
import com.orchid.orchid_marketplace.model.CartItem;

/**
 * Mapper for Cart and CartItem entities to DTOs.
 */
public final class CartMapper {
    
    private CartMapper() {
        // Utility class
    }
    
    /**
     * Convert Cart entity to CartResponse DTO.
     */
    public static CartResponse toResponse(Cart cart) {
        if (cart == null) {
            return null;
        }
        
        CartResponse response = new CartResponse();
        response.setItems(
            cart.getItems().stream()
                .map(CartMapper::toItemResponse)
                .collect(Collectors.toList())
        );
        response.setSubtotal(cart.getSubtotal());
        response.setTax(cart.getTax());
        response.setShippingCost(cart.getShippingCost());
        response.setTotal(cart.getTotal());
        response.setItemCount(cart.getItemCount());
        
        return response;
    }
    
    /**
     * Convert CartItem entity to CartItemResponse DTO.
     */
    public static CartItemResponse toItemResponse(CartItem item) {
        if (item == null) {
            return null;
        }
        
        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getTitle());
        response.setProductDescription(item.getProduct().getDescription());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setLineTotal(item.getLineTotal());
        response.setStockAvailable(item.getProduct().getStockQuantity());
        
        if (item.getShippingOption() != null) {
            response.setShippingOptionId(item.getShippingOption().getId());
            response.setShippingMethod(item.getShippingOption().getCarrier());
            response.setShippingCost(item.getShippingOption().getShippingCost());
        }
        
        return response;
    }
}
