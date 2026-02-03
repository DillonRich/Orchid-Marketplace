package com.orchid.orchid_marketplace.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.orchid.orchid_marketplace.dto.CartItemResponse;
import com.orchid.orchid_marketplace.dto.CartResponse;
import com.orchid.orchid_marketplace.model.Cart;
import com.orchid.orchid_marketplace.model.CartItem;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.ShippingOption;

class CartMapperTest {

    @Test
    void toResponse_mapsCartAndItems() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setTitle("Gadget");
        product.setDescription("Handy gadget");
        product.setStockQuantity(7);

        ShippingOption shipping = new ShippingOption();
        shipping.setId(UUID.randomUUID());
        shipping.setCarrier("UPS");
        shipping.setShippingCost(new BigDecimal("4.50"));

        CartItem item = new CartItem();
        item.setId(UUID.randomUUID());
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("9.99"));
        item.setLineTotal(new BigDecimal("19.98"));
        item.setShippingOption(shipping);

        Cart cart = new Cart();
        cart.setItems(List.of(item));
        cart.setSubtotal(new BigDecimal("19.98"));
        cart.setTax(new BigDecimal("1.23"));
        cart.setShippingCost(new BigDecimal("4.50"));
        cart.setTotal(new BigDecimal("25.71"));

        CartResponse response = CartMapper.toResponse(cart);

        assertEquals(cart.getSubtotal(), response.getSubtotal());
        assertEquals(cart.getTax(), response.getTax());
        assertEquals(cart.getShippingCost(), response.getShippingCost());
        assertEquals(cart.getTotal(), response.getTotal());
        assertEquals(cart.getItemCount(), response.getItemCount());

        CartItemResponse itemResponse = response.getItems().get(0);
        assertEquals(item.getId(), itemResponse.getCartItemId());
        assertEquals(product.getId(), itemResponse.getProductId());
        assertEquals(product.getTitle(), itemResponse.getProductName());
        assertEquals(product.getDescription(), itemResponse.getProductDescription());
        assertEquals(item.getQuantity(), itemResponse.getQuantity());
        assertEquals(item.getUnitPrice(), itemResponse.getUnitPrice());
        assertEquals(item.getLineTotal(), itemResponse.getLineTotal());
        assertEquals(product.getStockQuantity(), itemResponse.getStockAvailable());
        assertEquals(shipping.getId(), itemResponse.getShippingOptionId());
        assertEquals(shipping.getCarrier(), itemResponse.getShippingMethod());
        assertEquals(shipping.getShippingCost(), itemResponse.getShippingCost());
    }

    @Test
    void toItemResponse_handlesNull() {
        assertNull(CartMapper.toItemResponse(null));
    }
}
