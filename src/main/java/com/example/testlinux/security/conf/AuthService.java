package com.example.testlinux.security.conf;

import com.example.testlinux.domain.User;
import com.example.testlinux.exceptions.ValidationException;
import com.example.testlinux.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class AuthService {

    @Value("${app.jwt.current_secret}")
    private String jwtSecret;

    @Value("${app.jwt.old_secrets}")
    private String oldSecretsString;

    private Set<String> oldSecrets;

    public static final String AUTH_TOKEN_COOKIE_OLD_NAME = "token"; // Название старой куки с токеном. Нужно на переходный период, пока все не перелогинятся
    public static final String AUTH_TOKEN_COOKIE_NAME = "strikerstat_token";
    public static final Integer expiresInSeconds = 86400;

    @PostConstruct
    public void init() {
        log.info("Initializing AuthService - current-secret: " + jwtSecret);
        log.info("Initializing AuthService - old-secrets-string: " + oldSecretsString);
        if (oldSecretsString == null || oldSecretsString.isEmpty()) {
            oldSecrets = new HashSet<>();
        } else {
            oldSecrets = new HashSet<>(Arrays.asList(oldSecretsString.split(",")));
        }
        log.info("Initializing AuthService - old-secrets set: " + oldSecrets);
    }

    public JwtToken parseJwtToken(String token, boolean containsBearer) {
        return new JwtToken(token, containsBearer, jwtSecret, oldSecrets);
    }

    public Auth parseAuth(JwtToken jwt) {
        if (!jwt.isValid()) {
            return new Auth(null, null, false);
        }
        return new Auth(jwt.getUserId(), jwt.getRole(), true);
    }
}
