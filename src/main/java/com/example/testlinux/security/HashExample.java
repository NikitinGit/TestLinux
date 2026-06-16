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

        // Создаем экземпляр SHA-256
        MessageDigest digest1 = MessageDigest.getInstance("SHA-1");
        MessageDigest digest256 = MessageDigest.getInstance("SHA-256");

        // Хэшируем строку
        byte[] encodedHash = digest1.digest(input.getBytes(StandardCharsets.UTF_8));

        // Переводим байты в читаемый Hex-вид (шестнадцатеричную строку)
        StringBuilder hexString1 = getHexString1(encodedHash);
        System.out.println("SHA-1   Хэш: " + hexString1);

        byte[] encodedHash256 = digest256.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString256 = getHexString1(encodedHash256);
        System.out.println("SHA-256 Хэш: " + hexString256);

        //SHA-256 Хэш: 290c8ce73bc0d34ca6cc716a2d6cd7c288f9ae9f5839e9fddcb6daeba37e54c7
        //SHA-256 Хэш: 290c8ce73bc0d34ca6cc716a2d6cd7c288f9ae9f5839e9fddcb6daeba37e54c7
    }

    @NonNull
    private static StringBuilder getHexString1(byte[] encodedHash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString;
    }

}
