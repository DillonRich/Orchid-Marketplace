package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    
    List<Category> findByParentCategoryIsNull();
    List<Category> findByParentCategoryId(UUID parentId);
    List<Category> findByDisplayOrderGreaterThanEqual(Integer displayOrder);
    
    @Query("SELECT c FROM Category c WHERE c.isActive = true AND c.parentCategory IS NULL ORDER BY c.displayOrder ASC")
    List<Category> findActiveRootCategories();
    
    @Query("SELECT c FROM Category c WHERE c.isActive = true AND (c.parentCategory IS NULL OR c.parentCategory.id = :parentId) ORDER BY c.displayOrder ASC")
    List<Category> findActiveCategoriesByParent(@Param("parentId") UUID parentId);
    
    @Query("SELECT COUNT(p) FROM Category c JOIN c.products p WHERE c.id = :categoryId AND p.isActive = true")
    long countActiveProducts(@Param("categoryId") UUID categoryId);
    
    @Query("SELECT c FROM Category c WHERE c.isActive = true AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Category> searchCategories(@Param("keyword") String keyword);
}