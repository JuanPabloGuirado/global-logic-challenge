package com.globallogic.backendchallenge.application.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserTokenServiceTest {

    private UserTokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new UserTokenService();
        ReflectionTestUtils.setField(tokenService, "secretKey", "my-secret-key-abcdefghi-123456789");
    }

    @Test
    void generateTokenSuccessReturnTokenWithValidUuid() {
        String uuid = "test-uuid";
        String token = tokenService.generateToken(uuid);
        assertNotNull(token);
    }

    @Test
    void parseTokenReturnPayloadWhenTokenIsValid() {
        String uuid = "test-uuid";
        String token = tokenService.generateToken(uuid);
        Map<String, Object> payload = tokenService.parseToken(token);

        assertNotNull(payload);
        assertEquals(uuid, payload.get("uuid"));
    }
}