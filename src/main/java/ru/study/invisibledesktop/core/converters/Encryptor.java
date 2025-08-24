package ru.study.invisibledesktop.core.converters;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public interface Encryptor {

    default byte[] encrypt(byte[] bytes, byte[] key) throws Exception {
        SecretKeySpec spec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, spec);

        return cipher.doFinal(bytes);
    }

    default byte[] decrypt(byte[] bytes, byte[] key) throws Exception {
        SecretKeySpec spec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, spec);

        return cipher.doFinal(bytes);
    }

    default byte[] genKey(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            long seed = bytesToLong(Arrays.copyOfRange(hash, 0, 8));

            Random random = new Random(seed);

            byte[] key = new byte[16];
            random.nextBytes(key);

            return key;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available.", e);
        }
    }

    default long bytesToLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = (value << 8) | (bytes[i] & 0xFF);
        }
        return value;
    }

}

