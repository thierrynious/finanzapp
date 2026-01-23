package com.finanzmanager.finanzapp.config;

import com.finanzmanager.finanzapp.models.AppUser;
import com.finanzmanager.finanzapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initUsers() {
        return args -> {

            // Nur anlegen, wenn noch kein User existiert
            if (userRepository.findByEmail("admin@test.de").isEmpty()) {

                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setEmail("admin@test.de");
                admin.setPassword(
                        passwordEncoder.encode("123456")
                );

                userRepository.save(admin);
            }
        };
    }
}
