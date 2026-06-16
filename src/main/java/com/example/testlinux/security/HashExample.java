package com.example.testlinux.security;

import org.springframework.lang.NonNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashExample {
    public static void main(String[] arg) throws NoSuchAlgorithmException {
        String password = "bgn";
        String salt = "qX7z!";
        String input = salt + password;

        getHexString(input, "SHA-1");
        getHexString(input, "SHA-256");
        getHexString(input, "SHA3-224");
        getHexString(input, "SHA3-512");

        //SHA-256 Хэш: 290c8ce73bc0d34ca6cc716a2d6cd7c288f9ae9f5839e9fddcb6daeba37e54c7
        //SHA-256 Хэш: 290c8ce73bc0d34ca6cc716a2d6cd7c288f9ae9f5839e9fddcb6daeba37e54c7z
    }

    @NonNull
    private static StringBuilder getHexString(String input, String algorithm) throws NoSuchAlgorithmException {
        // Создаем экземпляр SHA-256
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        // Хэшируем строку
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        System.out.println(algorithm + " Хэш: " + hexString);

        return hexString;
    }

}
