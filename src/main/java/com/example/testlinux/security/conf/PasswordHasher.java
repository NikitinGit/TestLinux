package com.example.testlinux.security.conf;

//import org.bouncycastle.crypto.generators.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher {

    // Генерация хеша (эквивалентно PHP password_hash)
    public static String hash(String password) {
        String finalPassword = password == null ? "" : password;
        return BCrypt.withDefaults().hashToString(10, finalPassword.toCharArray());
    }

    // Проверка пароля (эквивалентно PHP password_verify)
    public static boolean isValid(String password, String hashedPassword) {
        String finalPassword = password == null ? "" : password;
        return BCrypt.verifyer().verify(finalPassword.toCharArray(), hashedPassword).verified;
    }

}
