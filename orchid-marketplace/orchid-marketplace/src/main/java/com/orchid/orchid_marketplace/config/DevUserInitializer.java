package com.orchid.orchid_marketplace.config;

/**
 * Dev-only initializer: creates a test user for local development.
 *
 * IMPORTANT: This class must be removed before production launch or guarded
 * behind a non-production-only activation mechanism.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.UserRepository;

@Component
@Order(1)
@Profile("local")
public class DevUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String email = "ciuser@example.com";
        if (!userRepository.existsByEmail(email)) {
            User u = new User(email, "CI User", Role.CUSTOMER);
            u.setPasswordHash(passwordEncoder.encode("Password1"));
            u.setIsActive(true);
            userRepository.save(u);
        }
    }
}
