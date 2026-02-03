package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.CategoryRequest;
import com.orchid.orchid_marketplace.dto.CategoryResponse;
import com.orchid.orchid_marketplace.model.Category;

public final class CategoryMapper {
    private CategoryMapper() {}

    public static Category toEntity(CategoryRequest req) {
        if (req == null) return null;
        Category c = new Category();
        c.setName(req.getName());
        return c;
    }

    public static CategoryResponse toResponse(Category c) {
        if (c == null) return null;
        CategoryResponse r = new CategoryResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        return r;
    }
}
