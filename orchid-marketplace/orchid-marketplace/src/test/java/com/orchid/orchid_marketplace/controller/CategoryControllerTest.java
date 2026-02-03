package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.CategoryRequest;
import com.orchid.orchid_marketplace.dto.CategoryResponse;
import com.orchid.orchid_marketplace.model.Category;
import com.orchid.orchid_marketplace.service.CategoryService;

class CategoryControllerTest {

    private CategoryController controller;

    @Mock
    private CategoryService categoryService;

    private UUID categoryId;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CategoryController();
        ReflectionTestUtils.setField(controller, "categoryService", categoryService);

        categoryId = UUID.randomUUID();
        testCategory = new Category();
        testCategory.setId(categoryId);
        testCategory.setName("Electronics");
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(categoryService.getAllCategories()).thenReturn(List.of(testCategory));

        List<CategoryResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        List<CategoryResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultipleCategories() {
        Category category2 = new Category();
        category2.setId(UUID.randomUUID());
        category2.setName("Books");

        when(categoryService.getAllCategories()).thenReturn(List.of(testCategory, category2));

        List<CategoryResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(categoryService.getCategoryById(categoryId)).thenReturn(Optional.of(testCategory));

        ResponseEntity<CategoryResponse> result = controller.getById(categoryId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(categoryService.getCategoryById(categoryId)).thenReturn(Optional.empty());

        ResponseEntity<CategoryResponse> result = controller.getById(categoryId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(categoryService.getCategoryById(categoryId)).thenReturn(Optional.of(testCategory));

        controller.getById(categoryId);

        verify(categoryService).getCategoryById(categoryId);
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        CategoryRequest request = new CategoryRequest();

        when(categoryService.createCategory(any(Category.class))).thenReturn(testCategory);

        CategoryResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        CategoryRequest request = new CategoryRequest();

        when(categoryService.createCategory(any(Category.class))).thenReturn(testCategory);

        controller.create(request);

        verify(categoryService).createCategory(any(Category.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        CategoryRequest request = new CategoryRequest();

        when(categoryService.updateCategory(eq(categoryId), any(Category.class))).thenReturn(testCategory);

        CategoryResponse result = controller.update(categoryId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        CategoryRequest request = new CategoryRequest();

        when(categoryService.updateCategory(eq(categoryId), any(Category.class))).thenReturn(testCategory);

        controller.update(categoryId, request);

        verify(categoryService).updateCategory(eq(categoryId), any(Category.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(categoryService).deleteCategory(categoryId);

        ResponseEntity<Void> result = controller.delete(categoryId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(categoryService).deleteCategory(categoryId);

        controller.delete(categoryId);

        verify(categoryService).deleteCategory(categoryId);
    }
}
