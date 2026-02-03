package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<User> searchActiveUsers(@Param("keyword") String keyword);
    
    Optional<User> findByStripeConnectAccountId(String stripeConnectAccountId);
    Optional<User> findByStripeCustomerId(String stripeCustomerId);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isActive = true")
    long countActiveByRole(@Param("role") Role role);
    
    @Query("SELECT u FROM User u WHERE u.entraId = :entraId")
    Optional<User> findByEntraId(@Param("entraId") String entraId);
}