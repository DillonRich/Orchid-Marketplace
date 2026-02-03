package com.orchid.orchid_marketplace.service;

import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.cosmos.CosmosUser;
import com.orchid.orchid_marketplace.repository.cosmos.CosmosUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("cosmos")
public class CosmosUserService {

    @Autowired
    private CosmosUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CosmosUser registerUser(String email, String fullName, String password, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        CosmosUser user = new CosmosUser(email, fullName, role);
        user.setId(UUID.randomUUID().toString());
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setIsActive(true);
        user.setIsEmailVerified(false);

        return userRepository.save(user);
    }

    public Optional<CosmosUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<CosmosUser> findById(String id) {
        return userRepository.findById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<CosmosUser> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<CosmosUser> findActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public List<CosmosUser> searchActiveUsers(String keyword) {
        return userRepository.searchActiveUsers(keyword);
    }

    public CosmosUser updateUser(CosmosUser user) {
        user.updateTimestamp();
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public boolean validatePassword(CosmosUser user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    public Optional<CosmosUser> findByStripeConnectAccountId(String accountId) {
        return userRepository.findByStripeConnectAccountId(accountId);
    }

    public Optional<CosmosUser> findByStripeCustomerId(String customerId) {
        return userRepository.findByStripeCustomerId(customerId);
    }

    public long countActiveByRole(Role role) {
        return userRepository.countActiveByRole(role);
    }
}
