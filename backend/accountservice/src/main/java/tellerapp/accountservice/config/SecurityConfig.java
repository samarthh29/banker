// In SecurityConfig (Account Service)
package tellerapp.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean(name = "accountServiceSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/account/**")  // Apply this filter only to /account/** paths
                .csrf().disable() // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all requests within this scope without authentication
                )
                .formLogin().disable(); // Disable the default login form
        return http.build();
    }
}
