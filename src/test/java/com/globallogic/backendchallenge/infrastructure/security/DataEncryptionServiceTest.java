package com.globallogic.backendchallenge.infrastructure.security;

import com.globallogic.backendchallenge.domain.exception.EncryptionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class DataEncryptionServiceTest {

    private DataEncryptionService dataEncryptionService;

    @BeforeEach
    void setUp() throws Exception {
        dataEncryptionService = new DataEncryptionService();
    }

    @Test
    void testEncryptValidPassword() {
        String password = "mySecurePassword";
        String encryptedPassword = dataEncryptionService.encrypt(password);

        assertNotNull(encryptedPassword, "Encrypted password should not be null");
        assertFalse(encryptedPassword.isEmpty(), "Encrypted password should not be empty");
        assertTrue(encryptedPassword.contains(":"), "Encrypted password should contain a colon");
    }

    @Test
    void testEncryptEmptyPassword() {
        String password = "";
        String encryptedPassword = dataEncryptionService.encrypt(password);

        assertNotNull(encryptedPassword, "Encrypted password should not be null");
        assertFalse(encryptedPassword.isEmpty(), "Encrypted password should not be empty");
    }

    @Test
    void testDecryptValidEncryptedPassword() {
        String password = "mySecurePassword";
        String encryptedPassword = dataEncryptionService.encrypt(password);
        String decryptedPassword = dataEncryptionService.decrypt(encryptedPassword);

        assertEquals(password, decryptedPassword, "Decrypted password should match the original password");
    }

    @Test
    void testDecryptInvalidEncryptedPasswordFormat() {
        String invalidEncryptedPassword = "invalidFormat";

        Exception exception = assertThrows(EncryptionException.class,
                () -> dataEncryptionService.decrypt(invalidEncryptedPassword));

        assertNotNull(exception, "EncryptionException should be thrown for invalid format");
    }

    @Test
    void testDecryptCorruptedIV() {
        String password = "mySecurePassword";
        String encryptedPassword = dataEncryptionService.encrypt(password);

        String corruptedEncryptedPassword = "corruptedIV:" + encryptedPassword.split(":")[1];

        Exception exception = assertThrows(EncryptionException.class,
                () -> dataEncryptionService.decrypt(corruptedEncryptedPassword));

        assertNotNull(exception, "EncryptionException should be thrown for corrupted IV");
    }

    @Test
    void testDecryptCorruptedCiphertext() {
        String password = "mySecurePassword";
        String encryptedPassword = dataEncryptionService.encrypt(password);

        String corruptedEncryptedPassword = encryptedPassword.split(":")[0] + ":corruptedCiphertext";

        Exception exception = assertThrows(EncryptionException.class,
                () -> dataEncryptionService.decrypt(corruptedEncryptedPassword));

        assertNotNull(exception, "EncryptionException should be thrown for corrupted ciphertext");
    }

    @Test
    void testEncryptExceptionHandling() throws NoSuchAlgorithmException {
        DataEncryptionService errorService = new DataEncryptionService() {
            @Override
            public String encrypt(String password) {
                throw new EncryptionException();
            }
        };

        Exception exception = assertThrows(EncryptionException.class,
                () -> errorService.encrypt("password"));

        assertNotNull(exception, "EncryptionException should be thrown during encryption failure");
    }

    @Test
    void testDecryptExceptionHandling() throws NoSuchAlgorithmException {
        DataEncryptionService errorService = new DataEncryptionService() {
            @Override
            public String decrypt(String encryptedPassword) {
                throw new EncryptionException();
            }
        };
        Exception exception = assertThrows(EncryptionException.class,
                () -> errorService.decrypt("invalidEncryptedPassword"));
        assertNotNull(exception, "EncryptionException should be thrown during decryption failure");
    }
}