package com.orchid.orchid_marketplace.repository.cosmos;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.cosmos.CosmosUser;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CosmosUserRepository extends CosmosRepository<CosmosUser, String> {
    
    Optional<CosmosUser> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<CosmosUser> findByRole(Role role);
    
    List<CosmosUser> findByIsActiveTrue();
    
    @Query("SELECT * FROM Users u WHERE u.isActive = true AND (CONTAINS(LOWER(u.email), LOWER(@keyword)) OR CONTAINS(LOWER(u.fullName), LOWER(@keyword)))")
    List<CosmosUser> searchActiveUsers(@Param("keyword") String keyword);
    
    Optional<CosmosUser> findByStripeConnectAccountId(String stripeConnectAccountId);
    
    Optional<CosmosUser> findByStripeCustomerId(String stripeCustomerId);
    
    @Query("SELECT VALUE COUNT(1) FROM Users u WHERE u.role = @role AND u.isActive = true")
    long countActiveByRole(@Param("role") Role role);
    
    Optional<CosmosUser> findByEntraId(String entraId);
}
