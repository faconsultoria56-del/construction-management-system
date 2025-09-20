package com.project.construction.configuration;

import com.project.construction.filter.JwtRequestFilter;
import com.project.construction.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/authenticate").permitAll()
                        // Employee endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/employees").hasAuthority(Role.MANAGER.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/employees/**").hasAuthority(Role.MANAGER.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/employees/**").hasAuthority(Role.MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").hasAnyAuthority(Role.MANAGER.name(), Role.WORKER.name())
                        // ConstructionSite endpoints
                        .requestMatchers("/api/v1/construction-sites/**").hasAuthority(Role.MANAGER.name())
                        // MaterialRequest endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/material-requests").hasAuthority(Role.WORKER.name())
                        .requestMatchers("/api/v1/material-requests/{id}/approve", "/api/v1/material-requests/{id}/reject").hasAuthority(Role.MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/material-requests/**").hasAnyAuthority(Role.MANAGER.name(), Role.WORKER.name())
                        // Finance endpoints
                        .requestMatchers("/api/v1/finance/transactions/{id}/approve", "/api/v1/finance/transactions/{id}/release").hasAuthority(Role.MANAGER.name())
                        .requestMatchers("/api/v1/finance/transactions").hasAnyAuthority(Role.MANAGER.name(), Role.OWNER.name())
                        // Partner endpoints
                        .requestMatchers("/api/v1/partners/**").hasAuthority(Role.OWNER.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
