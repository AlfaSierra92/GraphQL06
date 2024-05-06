package se.magnus.microservices.core.user.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange(exchanges -> exchanges
                                .pathMatchers(HttpMethod.GET,  "/user/**").permitAll()
                                .pathMatchers(HttpMethod.POST, "/user/**").permitAll()
                                .pathMatchers(HttpMethod.DELETE, "/user/**").permitAll()
                                .pathMatchers(HttpMethod.GET, "/auth/**").authenticated()
                        //.anyExchange().permitAll()
                )
                .httpBasic(withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(withDefaults());
        return http.build();
    }
}