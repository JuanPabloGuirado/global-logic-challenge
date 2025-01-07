package com.globallogic.backendchallenge.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Configuration
public class JwtConfig {

    @Value("${secret-key}")
    private String secretKey;

    @Bean
    public Key signingKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public SigningKeyResolver signingKeyResolver(Key signingKey) {
        return new SigningKeyResolver() {
            @Override
            public Key resolveSigningKey(JwsHeader header, Claims claims) {
                return signingKey;
            }

            @Override
            public Key resolveSigningKey(JwsHeader header, String plaintext) {
                return signingKey;
            }
        };
    }
}
