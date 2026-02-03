package com.orchid.orchid_marketplace.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchid.orchid_marketplace.dto.ProductRequest;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.service.ProductService;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    void create_withValidPayload_returnsCreatedProduct() throws Exception {
        ProductRequest req = new ProductRequest();
        req.setName("Test Orchid");
        req.setDescription("Lovely plant");
        req.setPrice(19.99);
        req.setStock(5);

        Product saved = new Product();
        saved.setId(UUID.randomUUID());
        saved.setTitle("Test Orchid");
        saved.setDescription("Lovely plant");
        saved.setPrice(BigDecimal.valueOf(19.99));
        saved.setStockQuantity(5);
        Store s = new Store();
        s.setId(UUID.randomUUID());
        saved.setStore(s);

        when(productService.createProduct(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/api/products")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
            .content(java.util.Objects.requireNonNull(objectMapper.writeValueAsString(req))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Orchid"))
                .andExpect(jsonPath("$.price").value(19.99));
    }
}
