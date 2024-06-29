package io.github.pbl32024.model.contentful;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ContentfulConfiguration {

    @Bean
    @Order(0)
    public SecurityFilterChain contentfulSecurityFilterChain(
            HttpSecurity http,
            @Value("${backend.contentful.password}") String password) throws Exception {
        return http
                .securityMatcher("/contentful/**")
                .authorizeHttpRequests(requests -> requests.requestMatchers("/contentful/*").authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(new InMemoryUserDetailsManager(User.builder()
                        .username("contentful")
                        .password(password)
                        .build()))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
