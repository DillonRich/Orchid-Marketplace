package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.Category;
import com.orchid.orchid_marketplace.repository.CategoryRepository;

@Service
@Profile("!cosmos")
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    // Get category by ID
    public Optional<Category> getCategoryById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return categoryRepository.findById(id);
    }
    
    // Get category by name
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    // Get categories by parent ID
    public List<Category> getCategoriesByParentId(UUID parentId) {
        Objects.requireNonNull(parentId, "parentId must not be null");
        return categoryRepository.findByParentCategoryId(parentId);
    }
    
    // Get root categories (no parent)
    public List<Category> getRootCategories() {
        return categoryRepository.findActiveRootCategories();
    }
    
    // Create a new category
    public Category createCategory(Category category) {
        Objects.requireNonNull(category, "category must not be null");
        // Check if category name already exists
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
        }
        
        @SuppressWarnings("null")
        Category saved = categoryRepository.save(category);
        return saved;
    }
    
    // Update a category
    public Category updateCategory(UUID id, Category categoryDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(categoryDetails, "categoryDetails must not be null");
        return categoryRepository.findById(id)
            .map(existingCategory -> {
                if (categoryDetails.getName() != null) {
                    // Check if new name already exists (excluding current category)
                    Optional<Category> existingWithName = categoryRepository.findByName(categoryDetails.getName());
                    if (existingWithName.map(c -> !c.getId().equals(id)).orElse(false)) {
                        throw new RuntimeException("Category with name '" + categoryDetails.getName() + "' already exists");
                    }
                    existingCategory.setName(categoryDetails.getName());
                }
                
                if (categoryDetails.getDescription() != null) {
                    existingCategory.setDescription(categoryDetails.getDescription());
                }
                
                if (categoryDetails.getDisplayOrder() != null) {
                    existingCategory.setDisplayOrder(categoryDetails.getDisplayOrder());
                }
                
                if (categoryDetails.getParentCategory() != null) {
                    // Check for circular reference
                    if (categoryDetails.getParentCategory().getId() == null || !categoryDetails.getParentCategory().getId().equals(id)) {
                        existingCategory.setParentCategory(categoryDetails.getParentCategory());
                    }
                } else {
                    existingCategory.setParentCategory(null);
                }

                @SuppressWarnings("null")
                Category saved = categoryRepository.save(existingCategory);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }
    
    // Soft delete a category
    public void deleteCategory(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        categoryRepository.findById(id)
            .ifPresentOrElse(
                category -> {
                    category.softDelete();
                    categoryRepository.save(category);
                },
                () -> { throw new RuntimeException("Category not found with ID: " + id); }
            );
    }
    
    // Search categories
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.searchCategories(keyword);
    }
    
    // Count active products in category
    public long countActiveProducts(UUID categoryId) {
        return categoryRepository.countActiveProducts(categoryId);
    }
    
    // Get categories by parent with pagination
    public List<Category> getActiveCategoriesByParent(UUID parentId) {
        return categoryRepository.findActiveCategoriesByParent(parentId);
    }
    
    // Count categories
    public long countCategories() {
        return categoryRepository.count();
    }
}