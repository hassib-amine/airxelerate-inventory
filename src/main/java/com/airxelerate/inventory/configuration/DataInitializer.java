package com.airxelerate.inventory.configuration;

import com.airxelerate.inventory.persistence.entity.user.Role;
import com.airxelerate.inventory.persistence.entity.user.User;
import com.airxelerate.inventory.persistence.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Data Initializer
 * Creates initial user accounts on application startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create Admin user (Account 1)
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            log.info("Created admin user: admin/admin123");
        }

        // Create Regular user (Account 2)
        if (!userRepository.existsByUsername("user")) {
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.ROLE_USER)
                    .enabled(true)
                    .build();
            userRepository.save(user);
            log.info("Created regular user: user/user123");
        }
    }
}

