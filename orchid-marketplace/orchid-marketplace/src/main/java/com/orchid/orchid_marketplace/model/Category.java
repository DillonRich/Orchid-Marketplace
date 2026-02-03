package com.orchid.orchid_marketplace.model;

import java.util.HashSet;
import java.util.Set;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(name = "display_name")
    private String displayName;
    
    private String description;
    
    @Column(name = "icon_class")
    private String iconClass;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;
    
    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private Set<Category> subCategories = new HashSet<>();
    
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    // Azure AI Search Integration
    @Column(name = "search_facet")
    private String searchFacet;
    
    // Cache Control
    @Column(name = "cache_key_prefix")
    private String cacheKeyPrefix = "category:";
    
    // ========== Constructors ==========
    
    public Category() {}
    
    public Category(String name, String displayName, String description) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }
    
    // ========== Getters and Setters ==========
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    public Category getParentCategory() {
        return parentCategory;
    }
    
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
    
    public Set<Category> getSubCategories() {
        return subCategories;
    }
    
    public void setSubCategories(Set<Category> subCategories) {
        this.subCategories = subCategories;
    }
    
    public Set<Product> getProducts() {
        return products;
    }
    
    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public String getSearchFacet() {
        return searchFacet;
    }
    
    public void setSearchFacet(String searchFacet) {
        this.searchFacet = searchFacet;
    }
    
    public String getCacheKeyPrefix() {
        return cacheKeyPrefix;
    }
    
    public void setCacheKeyPrefix(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix;
    }
    
    // Helper methods
    public void addSubCategory(Category subCategory) {
        subCategories.add(subCategory);
        subCategory.setParentCategory(this);
    }
    
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }
    
    public boolean hasParent() {
        return parentCategory != null;
    }
    
    public boolean hasSubCategories() {
        return subCategories != null && !subCategories.isEmpty();
    }
    
    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }
}