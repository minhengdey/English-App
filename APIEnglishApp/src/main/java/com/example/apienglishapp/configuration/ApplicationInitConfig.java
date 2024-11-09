package com.example.apienglishapp.configuration;

import com.example.apienglishapp.entity.UserEntity;
import com.example.apienglishapp.enums.Role;
import com.example.apienglishapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


@Configuration
@Slf4j
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Set<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                UserEntity userEntity = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(userEntity);
                log.warn("Admin has been created");
            }
        };
    }
}
