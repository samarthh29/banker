package tellerapp.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/transactions/withdraw/**"))
                        .hasAnyRole("Checker", "Authorizer")
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/transactions/deposit/**"))
                        .hasAnyRole("Maker", "Checker", "Authorizer")
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/accounts/**"))
                        .hasAnyRole("Maker", "Checker", "Authorizer")
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/customers/**"))
                        .hasAnyRole("Maker", "Checker", "Authorizer")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

            if (resourceAccess != null && resourceAccess.containsKey("api-gateway")) {
                Map<String, Object> apiGateway = (Map<String, Object>) resourceAccess.get("api-gateway");
                if (apiGateway.containsKey("roles")) {
                    List<String> roles = (List<String>) apiGateway.get("roles");
                    roles.forEach(role -> {
                        String authority = "ROLE_" + role;
                        authorities.add(new SimpleGrantedAuthority(authority));
                    });
                }
            }

            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }
}