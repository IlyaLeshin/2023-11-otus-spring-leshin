package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/", "/error", "/public/**", "/actuator/**").permitAll()
                        .requestMatchers("/books/creation-form", "/books/*/editing-form").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/books/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/authors").hasAuthority("ROLE_CAN_READ_AUTHORS")
                        .requestMatchers(HttpMethod.GET, "/api/v1/genres").hasAuthority("ROLE_CAN_READ_GENRES")
                        .requestMatchers("/api/v1/books/*/comments/**").hasAuthority("ROLE_CAN_EDIT_COMMENTS")
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/**").hasAuthority("ROLE_CAN_READ_BOOKS")
                        .requestMatchers("/api/v1/books/**").hasAuthority("ROLE_CAN_EDIT_BOOKS")
                        .anyRequest().denyAll()
                )
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
