package com.tellerapp.customer_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all requests without authentication
                )
                .formLogin().disable(); // Disable the default login form
        return http.build();
    }
}
