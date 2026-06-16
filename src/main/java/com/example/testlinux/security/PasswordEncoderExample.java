package com.example.testlinux.security;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * Современные алгоритмы хеширования ПАРОЛЕЙ (Spring Security).
 *
 * Чем отличается от {@link HashExample} (SHA-1/SHA-256):
 *   SHA-* спроектированы БЫСТРЫМИ — атакующий перебирает миллиарды вариантов/сек на GPU.
 *   Алгоритмы ниже НАМЕРЕННО МЕДЛЕННЫЕ и "дорогие" по CPU/памяти — это защита от перебора.
 *
 * Общие свойства всех PasswordEncoder в Spring:
 *   - encode(raw)            -> хеш (соль генерируется ВНУТРИ и встраивается в строку результата);
 *   - matches(raw, encoded)  -> сравнение; соль и параметры берутся из самой строки encoded;
 *   - одинаковый пароль каждый раз даёт РАЗНЫЙ хеш (соль случайная) — сравнивать "в лоб" нельзя,
 *     только через matches().
 *
 * Зависимости: Argon2 и SCrypt требуют BouncyCastle (bcprov) на classpath.
 */
public class PasswordEncoderExample {

    public static void main(String[] args) {
        String password = "S3cr3t-пароль";

        bcrypt(password);
        argon2(password);
        scrypt(password);
        pbkdf2(password);
        delegating(password);
    }

    /**
     * BCrypt — отраслевой стандарт по умолчанию, основан на шифре Blowfish.
     * Параметр "strength" (cost / log-rounds) = 10 по умолчанию: 2^10 = 1024 итерации.
     * Каждое +1 к strength удваивает время. Соль (16 байт) встроена в результат.
     *
     * Формат: $2a$10$<22 символа соли><31 символ хеша>
     *   $2a$ — версия, 10 — strength.
     * Ограничение: учитывает только первые 72 байта пароля.
     */
    private static void bcrypt(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // strength=10
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        System.out.println("=== BCrypt ===");
        System.out.println("hash #1 : " + hash1);
        System.out.println("hash #2 : " + hash2 + "   <- другой! (случайная соль)");
        System.out.println("matches(правильный) = " + encoder.matches(password, hash1)); // true
        System.out.println("matches(неверный)   = " + encoder.matches("wrong", hash1));  // false
        System.out.println();
    }

    /**
     * Argon2 — победитель Password Hashing Competition (2015), РЕКОМЕНДУЕТСЯ для нового кода.
     * Стоек к атакам на GPU/ASIC, т.к. требует много ПАМЯТИ (memory-hard).
     * Три параметра: saltLength, hashLength, parallelism, memory (KB), iterations.
     *
     * Формат: $argon2id$v=19$m=16384,t=2,p=1$<соль>$<хеш>
     */
    private static void argon2(String password) {
        // фабрика с дефолтными параметрами под Spring Security 6 (argon2id, m=16384, t=2, p=1)
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String hash = encoder.encode(password);

        System.out.println("=== Argon2 (argon2id) — рекомендуется ===");
        System.out.println("hash    : " + hash);
        System.out.println("matches = " + encoder.matches(password, hash));
        System.out.println();
    }

    /**
     * SCrypt — тоже memory-hard, предшественник Argon2 (используется в т.ч. в криптовалютах).
     * Параметры: cpuCost (N), memoryCost (r), parallelization (p), keyLength, saltLength.
     *
     * Формат: $<params>$<соль>$<хеш> (base64).
     */
    private static void scrypt(String password) {
        SCryptPasswordEncoder encoder = SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8();
        String hash = encoder.encode(password);

        System.out.println("=== SCrypt ===");
        System.out.println("hash    : " + hash);
        System.out.println("matches = " + encoder.matches(password, hash));
        System.out.println();
    }

    /**
     * PBKDF2 — старый, но всё ещё одобренный (NIST/FIPS) алгоритм.
     * НЕ memory-hard (уязвимее к GPU, чем Argon2/SCrypt), но прост и есть в JDK.
     * Берёт базовый хеш (HMAC-SHA256) и прогоняет его N итераций.
     *
     * Хранит соль + параметры внутри строки. Здесь — secret="" (без доп. ключа),
     * 16 байт соли, 310000 итераций, SHA-256.
     */
    private static void pbkdf2(String password) {
        Pbkdf2PasswordEncoder encoder =
                Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8(); // 310000 итераций, SHA-256
        String hash = encoder.encode(password);

        System.out.println("=== PBKDF2 ===");
        System.out.println("hash    : " + hash);
        System.out.println("matches = " + encoder.matches(password, hash));
        System.out.println();
    }

    /**
     * DelegatingPasswordEncoder — правильный способ для реального приложения.
     * Хеш получает префикс с именем алгоритма: {bcrypt}$2a$10$...  или  {argon2}$argon2id$...
     *
     * Зачем:
     *   - можно МИГРИРОВАТЬ алгоритмы без сброса паролей: новые пароли пишутся текущим
     *     алгоритмом, старые проверяются по своему префиксу;
     *   - matches() сам выбирает нужный энкодер по префиксу.
     * Это то, что возвращает PasswordEncoderFactories.createDelegatingPasswordEncoder()
     * и что Spring Boot ставит бином PasswordEncoder по умолчанию.
     */
    private static void delegating(String password) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String hash = encoder.encode(password);

        System.out.println("=== DelegatingPasswordEncoder (по умолчанию -> bcrypt) ===");
        System.out.println("hash    : " + hash + "   <- обрати внимание на префикс {bcrypt}");
        System.out.println("matches = " + encoder.matches(password, hash));

        // matches видит префикс и проверяет даже хеш, сделанный ДРУГИМ алгоритмом
        String argonHash = "{argon2}" + Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().encode(password);
        System.out.println("matches(argon2-хеш) = " + encoder.matches(password, argonHash)); // true
    }
}
