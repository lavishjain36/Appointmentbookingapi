package com.example.appointment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String APPOINTMENTS = "/api/appointments";
    private static final String APPOINTMENT_BY_ID = "/api/appointments/*";
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, APPOINTMENTS).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, APPOINTMENTS).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, APPOINTMENTS + "/available").hasAnyRole(USER, ADMIN)
                        .requestMatchers(HttpMethod.POST, APPOINTMENT_BY_ID + "/book").hasRole(USER)
                        .requestMatchers(HttpMethod.GET, APPOINTMENTS + "/my").hasRole(USER)
                        .requestMatchers(HttpMethod.PUT, APPOINTMENT_BY_ID + "/cancel").hasRole(USER)
                        .anyRequest().authenticated())
                .httpBasic(basic -> {})
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, error) -> response.sendError(401, "Unauthorized"))
                        .accessDeniedHandler((request, response, error) -> response.sendError(403, "Forbidden")));
        return http.build();
    }

    // Demo-only users for HTTP Basic authentication.
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .roles(ADMIN)
                        .build(),
                User.withUsername("user@example.com")
                        .password(passwordEncoder.encode("user123"))
                        .roles(USER)
                        .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
