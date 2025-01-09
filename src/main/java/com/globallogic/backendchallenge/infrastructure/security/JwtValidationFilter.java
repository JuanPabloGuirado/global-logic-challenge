package com.globallogic.backendchallenge.infrastructure.security;

import com.globallogic.backendchallenge.domain.dto.ErrorResponse;
import com.globallogic.backendchallenge.utils.ErrorCode;
import com.google.gson.*;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final SigningKeyResolver signingKeyResolver;
    private final Gson gson;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(gson.toJson(getErrorResponse()));
            response.getWriter().flush();
            return;
        }
        String trimmedToken = Optional.of(authorizationHeader)
                .filter(str -> str.length() > 7)
                .map(str -> str.substring(7))
                .orElse(Strings.EMPTY);

        try {
            Jwts.parserBuilder()
                    .setSigningKeyResolver(signingKeyResolver)
                    .build()
                    .parseClaimsJws(trimmedToken);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error(String.format("Invalid jwt: %s", trimmedToken));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(gson.toJson(getErrorResponse()));
            response.getWriter().flush();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/users/sign-up") || path.startsWith("/h2-console");
    }

    private ErrorResponse getErrorResponse() {
        return ErrorResponse.builder()
                .error(ErrorResponse.InnerError.builder()
                        .code(ErrorCode.UNAUTHORIZED.getCode())
                        .detail("Missing or invalid Authorization header")
                        .timestamp(LocalDateTime.now())
                        .build())
                .build();
    }
}
