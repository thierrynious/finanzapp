package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.models.User;
import com.finanzmanager.finanzapp.repository.UserRepository;
import com.finanzmanager.finanzapp.security.CustomUserDetails;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Benutzer nicht gefunden: " + username));

        return new CustomUserDetails(user);
    }
}
