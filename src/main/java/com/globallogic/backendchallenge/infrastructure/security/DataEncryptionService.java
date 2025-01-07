package com.globallogic.backendchallenge.infrastructure.security;

import com.globallogic.backendchallenge.domain.exception.EncryptionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class DataEncryptionService {

    private final SecretKey secretKey;

    public DataEncryptionService() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        this.secretKey = keyGenerator.generateKey();
    }

    public String encrypt(String password) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();
            byte[] encryptedPassword = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encryptedPassword);
        } catch (GeneralSecurityException exception) {
            log.error(String.format("Error during password encryption - message: %s",
                    exception.getLocalizedMessage()));
            throw new EncryptionException();
        }
    }

    public String decrypt(String encryptedPassword) {
        try {
            String[] parts = encryptedPassword.split(":");
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] encryptedPasswordBytes = Base64.getDecoder().decode(parts[1]);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            byte[] originalPassword = cipher.doFinal(encryptedPasswordBytes);
            return new String(originalPassword);
        } catch (GeneralSecurityException exception) {
            log.error(String.format("Error during password decryption - message: %s",
                    exception.getLocalizedMessage()));
            throw new EncryptionException();
        }
    }
}
