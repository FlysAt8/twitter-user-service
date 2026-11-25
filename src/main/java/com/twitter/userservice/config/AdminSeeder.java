package com.twitter.userservice.config;

import com.twitter.userservice.entity.Role;
import com.twitter.userservice.entity.User;
import com.twitter.userservice.repository.RoleRepository;
import com.twitter.userservice.repository.UserRepository;
import com.twitter.userservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@twitter.local";
    private static final String ADMIN_PASSWORD = "admin";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        User admin = userRepository.findByEmail(ADMIN_EMAIL).orElseGet(() -> {
            User created = userRepository.save(User.builder()
                    .firstname("Admin")
                    .lastname("Adminov")
                    .email(ADMIN_EMAIL)
                    .phone("+70000000000")
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .createdAt(LocalDateTime.now())
                    .build());
            log.info("Создан пользователь Admin: {}", ADMIN_EMAIL);
            return created;
        });

        for (Role role : roleRepository.findAll()) {
            if (!userRoleRepository.exists(admin.getId(), role.getId())) {
                userRoleRepository.assignRole(admin.getId(), role.getId());
                log.info("Admin получил роль: {}", role.getName());
            }
        }
    }
}