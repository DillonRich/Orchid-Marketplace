package com.orchid.orchid_marketplace.mapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.orchid.orchid_marketplace.dto.ProductRequest;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.util.InputSanitizer;

public class ProductMapperTest {

    @Test
    void toEntity_mapsAndSanitizesFields() {
        ProductRequest req = new ProductRequest();
        req.setName("<script>alert(1)</script>My Orchid");
        req.setDescription("<b>Nice</b> description");
        req.setPrice(12.5);
        req.setStock(3);

        Product p = ProductMapper.toEntity(req);

        assertNotNull(p);
        assertEquals(InputSanitizer.sanitize(req.getName()), p.getTitle());
        assertEquals(InputSanitizer.sanitize(req.getDescription()), p.getDescription());
        assertEquals(BigDecimal.valueOf(req.getPrice()), p.getPrice());
        assertEquals(req.getStock(), p.getStockQuantity());
    }

    @Test
    void toEntity_nullRequest_returnsNull() {
        assertNull(ProductMapper.toEntity(null));
    }
}
