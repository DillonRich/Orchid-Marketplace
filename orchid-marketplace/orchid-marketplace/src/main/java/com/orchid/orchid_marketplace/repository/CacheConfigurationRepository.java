package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.CacheConfiguration;

@Repository
public interface CacheConfigurationRepository extends JpaRepository<CacheConfiguration, UUID> {
    
    List<CacheConfiguration> findByEntityType(String entityType);
    List<CacheConfiguration> findByIsEnabledTrue();
    List<CacheConfiguration> findByEntityTypeAndIsEnabledTrue(String entityType);
    
    @Query("SELECT cc FROM CacheConfiguration cc WHERE cc.isEnabled = true AND cc.cacheTtlSeconds >= :minTtl")
    List<CacheConfiguration> findEnabledByMinTtl(@Param("minTtl") Integer minTtl);
    
    @Query("SELECT cc FROM CacheConfiguration cc WHERE cc.cacheStrategy = :strategy AND cc.isEnabled = true")
    List<CacheConfiguration> findByStrategy(@Param("strategy") String strategy);
    
    @Query("SELECT cc FROM CacheConfiguration cc WHERE cc.entityType = :entityType AND cc.cacheKeyPattern LIKE CONCAT('%', :pattern, '%') AND cc.isEnabled = true")
    List<CacheConfiguration> findByEntityAndPattern(@Param("entityType") String entityType, @Param("pattern") String pattern);
    
    boolean existsByCacheKeyPatternAndIsEnabledTrue(String cacheKeyPattern);
}