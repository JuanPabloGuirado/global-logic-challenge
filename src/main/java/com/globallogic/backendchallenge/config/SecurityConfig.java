package com.globallogic.backendchallenge.config;

import com.globallogic.backendchallenge.infrastructure.security.JwtValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtValidationFilter jwtFilter)
            throws Exception {
        http
                .csrf()
                .disable() // Disable CSRF for H2 console
                .headers()
                .frameOptions().sameOrigin() // Allow H2 console to display in frames
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users/sign-up").permitAll()
                .antMatchers(HttpMethod.GET, "/users/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
