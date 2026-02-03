package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.UserRepository;

@Service
@Profile("!cosmos")
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Get user by ID
    public Optional<User> getUserById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return userRepository.findById(id);
    }
    
    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Get users by role
    public List<User> getUsersByRole(Role role) {
        Objects.requireNonNull(role, "role must not be null");
        return userRepository.findByRole(role);
    }
    
    // Search users by keyword (email or name)
    public List<User> searchUsers(String keyword) {
        return userRepository.searchActiveUsers(keyword);
    }
    
    // Create a new user (expects raw password in passwordHash field or null)
    public User createUser(User user) {
        Objects.requireNonNull(user, "user must not be null");
        if (user.getEmail() == null) throw new RuntimeException("Email is required");
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email '" + user.getEmail() + "' already exists");
        }
        // If a raw password is provided in passwordHash, encode it
        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        @SuppressWarnings("null")
        User saved = userRepository.save(user);
        return saved;
    }
    
    // Update a user
    public User updateUser(UUID id, User userDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(userDetails, "userDetails must not be null");
        return userRepository.findById(id)
            .map(existingUser -> {
                if (userDetails.getEmail() != null) {
                    // Check if new email already exists (excluding current user)
                    Optional<User> existingWithEmail = userRepository.findByEmail(userDetails.getEmail());
                    if (existingWithEmail.map(u -> !u.getId().equals(id)).orElse(false)) {
                        throw new RuntimeException("User with email '" + userDetails.getEmail() + "' already exists");
                    }
                    existingUser.setEmail(userDetails.getEmail());
                }
                
                if (userDetails.getFullName() != null) {
                    existingUser.setFullName(userDetails.getFullName());
                }
                
                if (userDetails.getPhoneNumber() != null) {
                    existingUser.setPhoneNumber(userDetails.getPhoneNumber());
                }
                
                if (userDetails.getRole() != null) {
                    existingUser.setRole(userDetails.getRole());
                }
                
                if (userDetails.getPasswordHash() != null) {
                    existingUser.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
                }
                
                if (userDetails.getEntraId() != null) {
                    existingUser.setEntraId(userDetails.getEntraId());
                }

                @SuppressWarnings("null")
                User saved = userRepository.save(existingUser);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
    
    // Soft delete a user
    public void deleteUser(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        userRepository.findById(id)
            .ifPresentOrElse(
                user -> {
                    user.softDelete();
                    userRepository.save(user);
                },
                () -> { throw new RuntimeException("User not found with ID: " + id); }
            );
    }
    
    // Toggle user active status
    public User toggleUserStatus(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return userRepository.findById(id)
            .map(user -> {
                user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
                @SuppressWarnings("null")
                User saved = userRepository.save(user);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
    
    // Get total number of users
    public long countUsers() {
        return userRepository.count();
    }
    
    // Count active users by role
    public long countActiveByRole(Role role) {
        Objects.requireNonNull(role, "role must not be null");
        return userRepository.countActiveByRole(role);
    }
    
    // Find user by Stripe Connect Account ID
    public Optional<User> findByStripeConnectAccountId(String stripeConnectAccountId) {
        return userRepository.findByStripeConnectAccountId(stripeConnectAccountId);
    }
    
    // Find user by Stripe Customer ID
    public Optional<User> findByStripeCustomerId(String stripeCustomerId) {
        return userRepository.findByStripeCustomerId(stripeCustomerId);
    }
    
    // Find user by Entra ID
    public Optional<User> findByEntraId(String entraId) {
        return userRepository.findByEntraId(entraId);
    }
    
    // Get active users
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
}