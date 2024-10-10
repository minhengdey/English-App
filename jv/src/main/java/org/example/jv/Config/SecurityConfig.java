package org.example.jv.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
                .requestMatchers("/user").permitAll()
                .requestMatchers("/user/**").permitAll()
                .requestMatchers("/new_word").permitAll()
                .requestMatchers("/new_word/**").permitAll()
                .anyRequest().authenticated()
        );
        return http.build();
    }
}