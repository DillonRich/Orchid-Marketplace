package com.orchid.orchid_marketplace.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.UserRepository;

@Service
@Profile("!cosmos")
public class SpringUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPasswordHash() == null ? "" : user.getPasswordHash(),
            user.getIsActive() != null && user.getIsActive(),
            true, true, true,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + (user.getRole() != null ? user.getRole().name() : "USER"))));
    }
}
