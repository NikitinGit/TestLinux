package com.example.testlinux.service;

import com.example.testlinux.domain.User;
import com.example.testlinux.exceptions.ApiValidationException;
import com.example.testlinux.security.conf.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

import static com.example.testlinux.security.conf.AuthService.AUTH_TOKEN_COOKIE_NAME;
import static com.example.testlinux.security.conf.AuthService.AUTH_TOKEN_COOKIE_OLD_NAME;

@Service
@RequiredArgsConstructor
public class AuthSessionService {

    public record SessionTokenChange(String token, Integer userId, String role, List<ResponseCookie> cookies) {}

    private final UserTokenStoreService userTokenStoreService;
    private final ObjectMapper objectMapper;
    private final AuditLogger auditLogger;

    @Value("${app.jwt.current_secret}")
    private String jwtSecret;

    @Value("${app.jwt.old_secrets:}")
    private String oldSecretsString;

    @Value("${app.auth.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${app.auth.cookie.domain:}")
    private String cookieDomain;

    private Set<String> oldSecrets = Set.of();

    @PostConstruct
    public void init() {
        if (oldSecretsString == null || oldSecretsString.isBlank()) {
            oldSecrets = Set.of();
            return;
        }
        oldSecrets = new LinkedHashSet<>(Arrays.asList(oldSecretsString.split(",")));
    }

    public SessionTokenChange restoreOriginalSession(String rawToken, HttpServletRequest request) {
        JwtToken currentToken = parseToken(rawToken);
        if (!currentToken.isValid() || currentToken.getOriginalUserId() == null || currentToken.getOriginalRole() == null
                || currentToken.getOriginalExpiration() == null || !currentToken.originalIsValid()) {
            return null;
        }

        Integer currentUserId = currentToken.getUserId();
        if (currentUserId == null) return null;

        UserTokenStoreService.TokenClientInfo clientInfo = userTokenStoreService.findClientInfo(currentUserId, rawToken);
        // Как и в PHP, выход из impersonate разрешаем только для токена, который реально есть в белом списке.
        // Иначе валидный, но уже отозванный JWT смог бы породить новую админскую сессию.
        if (clientInfo == null) return null;
        userTokenStoreService.delete(currentUserId, rawToken);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put(JwtToken.PAYLOAD_USER_ID, currentToken.getOriginalUserId());
        payload.put(JwtToken.PAYLOAD_ROLE, currentToken.getOriginalRole());
        payload.put("exp", currentToken.getOriginalExpiration());

        String restoredToken = createSignedToken(payload);
        String userAgent = clientInfo.userAgent() == null || clientInfo.userAgent().isBlank() ? resolveUserAgent(request) : clientInfo.userAgent();
        String ip = clientInfo.ip() == null || clientInfo.ip().isBlank() ? resolveClientIp(request) : clientInfo.ip();
        userTokenStoreService.create(currentToken.getOriginalUserId(), restoredToken, Instant.ofEpochSecond(currentToken.getOriginalExpiration()), userAgent, ip);
        auditLogger.log("leave_impersonation", currentUserId, "users", currentToken.getOriginalUserId().longValue(),
                Map.of("prev_user_id", currentUserId, "prev_user_role", currentToken.getRole()),
                Map.of("new_user_id", currentToken.getOriginalUserId(), "new_user_role", currentToken.getOriginalRole()));

        int cookieLifetime = secondsUntil(currentToken.getOriginalExpiration());
        List<ResponseCookie> cookies = new ArrayList<>(buildSessionCookies(
                restoredToken, cookieLifetime, currentToken.getOriginalUserId(), currentToken.getOriginalRole(), request));
        cookies.addAll(buildImpersonatingFlagCookies(0, request));
        return new SessionTokenChange(restoredToken, currentToken.getOriginalUserId(), currentToken.getOriginalRole(), cookies);
    }

    private ResponseCookie buildAdminTokenCookie(String adminToken, long maxAgeSeconds, String domain, boolean secure) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("strikerstat_admin_token", adminToken == null ? "" : adminToken)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds);
        if (domain != null) builder.domain(domain);
        return builder.build();
    }

    public List<ResponseCookie> buildImpersonatingFlagCookies(long maxAgeSeconds, HttpServletRequest request) {
        List<ResponseCookie> cookies = new ArrayList<>();
        String primaryDomain = resolvePrimaryCookieDomain();
        boolean secure = resolveCookieSecure(request);
        cookies.add(buildImpersonatingFlagCookie(maxAgeSeconds, primaryDomain, secure));
        addLegacyDeletionCookies(cookies, request, primaryDomain, domain -> buildImpersonatingFlagCookie(0, domain, secure));
        return cookies;
    }

    private ResponseCookie buildImpersonatingFlagCookie(long maxAgeSeconds, String domain, boolean secure) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("strikerstat_impersonating", maxAgeSeconds > 0 ? "1" : "")
                .httpOnly(false)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds);
        if (domain != null) builder.domain(domain);
        return builder.build();
    }

    public String createPasswordResetToken(User user, int lifetimeSeconds) {
        long exp = Instant.now().getEpochSecond() + lifetimeSeconds;
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("uid", user.getId());
        payload.put("exp", (int) exp);
        payload.put("hash", user.getHashForPasswordChange(String.valueOf(exp)));
        return createSignedToken(payload);
    }

    public Integer extractUserIdFromPasswordResetToken(String token) {
        if (token == null || token.isBlank()) return null;
        JwtToken jwtToken = new JwtToken(token, false, jwtSecret, oldSecrets);
        if (!jwtToken.isValid()) return null;
        Object uid = jwtToken.getPayloadMap().get("uid");
        return uid instanceof Number n ? n.intValue() : null;
    }

    public void validatePasswordResetToken(User user, String token) {
        if (token == null || token.isBlank()) {
            throw new ApiValidationException("Токен для установки нового пароля обязателен.");
        }

        JwtToken jwtToken = new JwtToken(token, false, jwtSecret, oldSecrets);
        if (!jwtToken.isValid()) {
            throw new ApiValidationException("Ссылка для установки нового пароля истекла или недействительна.");
        }

        Map<String, Object> payload = jwtToken.getPayloadMap();
        Object expRaw = payload.get("exp");
        Object hashRaw = payload.get("hash");
        if (!(expRaw instanceof Number expNumber) || hashRaw == null) {
            throw new ApiValidationException("Токен для установки нового пароля поврежден.");
        }

        long exp = expNumber.longValue();
        if (exp < Instant.now().getEpochSecond()) {
            throw new ApiValidationException("Ссылка для установки нового пароля истекла.");
        }

        String expectedHash = user.getHashForPasswordChange(String.valueOf(exp));
        if (!expectedHash.equals(String.valueOf(hashRaw))) {
            throw new ApiValidationException("Ссылка для установки нового пароля недействительна.");
        }
    }

    public String extractRawToken(HttpServletRequest request) {
        if (request == null) return null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTH_TOKEN_COOKIE_NAME.equals(cookie.getName())) return cookie.getValue();
            }
            for (Cookie cookie : cookies) {
                if (AUTH_TOKEN_COOKIE_OLD_NAME.equals(cookie.getName())) return cookie.getValue();
            }
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) return null;
        return authorization.regionMatches(true, 0, "Bearer ", 0, 7) ? authorization.substring(7).trim() : authorization.trim();
    }

    private List<ResponseCookie> buildSessionCookies(String token, int lifetimeSeconds, Integer userId, String role,
                                                     HttpServletRequest request) {
        List<ResponseCookie> cookies = new ArrayList<>();
        String primaryDomain = resolvePrimaryCookieDomain();
        boolean secure = resolveCookieSecure(request);
        cookies.add(buildAuthCookie(token, lifetimeSeconds, primaryDomain, secure));
        cookies.add(buildReadableCookie("login", userId == null ? "" : String.valueOf(userId), lifetimeSeconds, primaryDomain, secure));
        cookies.add(buildReadableCookie("accstatus", role == null ? "" : role, lifetimeSeconds, primaryDomain, secure));
        cookies.add(buildReadableCookie(AUTH_TOKEN_COOKIE_OLD_NAME, "", 0, primaryDomain, secure));
        addLegacyDeletionCookies(cookies, request, primaryDomain, domain -> buildAuthCookie("", 0, domain, secure));
        addLegacyDeletionCookies(cookies, request, primaryDomain, domain -> buildReadableCookie("login", "", 0, domain, secure));
        addLegacyDeletionCookies(cookies, request, primaryDomain, domain -> buildReadableCookie("accstatus", "", 0, domain, secure));
        addLegacyDeletionCookies(cookies, request, primaryDomain, domain -> buildReadableCookie(AUTH_TOKEN_COOKIE_OLD_NAME, "", 0, domain, secure));
        addLegacyDeletionCookies(cookies, request, primaryDomain, domain -> buildAdminTokenCookie("", 0, domain, secure));
        addLegacyDeletionCookies(cookies, request, primaryDomain, domain -> buildImpersonatingFlagCookie(0, domain, secure));
        return cookies;
    }

    private ResponseCookie buildAuthCookie(String token, long maxAgeSeconds, String domain, boolean secure) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(AUTH_TOKEN_COOKIE_NAME, token == null ? "" : token)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds);
        if (domain != null) builder.domain(domain);
        return builder.build();
    }

    private ResponseCookie buildReadableCookie(String name, String value, long maxAgeSeconds, String domain, boolean secure) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value == null ? "" : value)
                .httpOnly(false)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds);
        if (domain != null) builder.domain(domain);
        return builder.build();
    }

    private void addSessionDeletionCookies(List<ResponseCookie> cookies, String domain, boolean secure) {
        cookies.add(buildAuthCookie("", 0, domain, secure));
        cookies.add(buildReadableCookie("login", "", 0, domain, secure));
        cookies.add(buildReadableCookie("accstatus", "", 0, domain, secure));
        cookies.add(buildReadableCookie(AUTH_TOKEN_COOKIE_OLD_NAME, "", 0, domain, secure));
        cookies.add(buildAdminTokenCookie("", 0, domain, secure));
        cookies.add(buildImpersonatingFlagCookie(0, domain, secure));
    }

    private String resolvePrimaryCookieDomain() {
        return formatCookieDomain(normalizeCookieDomain(cookieDomain));
    }

    private Set<String> resolveLegacyCookieDomains(HttpServletRequest request) {
        LinkedHashSet<String> domains = new LinkedHashSet<>();
        String configuredDomain = formatCookieDomain(normalizeCookieDomain(cookieDomain));
        if (configuredDomain != null) domains.add(configuredDomain);

        // На preprod исторически могли остаться cookie с Domain, равным точному host.
        // Если не удалить и этот вариант, браузер продолжит отправлять старую, более специфичную cookie.
        String requestHostDomain = formatCookieDomain(normalizeCookieDomain(request == null ? null : request.getServerName()));
        if (requestHostDomain != null) {
            domains.add(requestHostDomain);
        }
        return domains;
    }

    private void addLegacyDeletionCookies(List<ResponseCookie> cookies, HttpServletRequest request, String primaryDomain,
                                          java.util.function.Function<String, ResponseCookie> cookieFactory) {
        if (primaryDomain != null) {
            cookies.add(cookieFactory.apply(null));
        }
        for (String domain : resolveLegacyCookieDomains(request)) {
            if (Objects.equals(domain, primaryDomain)) continue;
            cookies.add(cookieFactory.apply(domain));
        }
    }

    private boolean shouldUseExplicitCookieDomain(String domain) {
        if (domain == null || domain.isBlank() || "localhost".equalsIgnoreCase(domain)) return false;
        return domain.chars().anyMatch(ch -> ch == '.');
    }

    private String normalizeCookieDomain(String domain) {
        if (domain == null) return null;
        String normalized = domain.trim().toLowerCase(Locale.ROOT);
        while (normalized.startsWith(".")) normalized = normalized.substring(1);
        return normalized.isBlank() ? null : normalized;
    }

    private String formatCookieDomain(String domain) {
        if (domain == null || !shouldUseExplicitCookieDomain(domain)) return null;
        return "." + domain;
    }

    private JwtToken parseToken(String rawToken) {
        return new JwtToken(rawToken, false, jwtSecret, oldSecrets);
    }

    private int secondsUntil(long epochSecond) {
        return (int) Math.max(0, epochSecond - Instant.now().getEpochSecond());
    }

    private boolean resolveCookieSecure(HttpServletRequest request) {
        if (cookieSecure) return true;
        if (request == null) return false;
        if (request.isSecure()) return true;

        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        if (forwardedProto != null) {
            for (String value : forwardedProto.split(",")) {
                if ("https".equalsIgnoreCase(value.trim())) return true;
            }
        }

        String forwardedSsl = request.getHeader("X-Forwarded-Ssl");
        if ("on".equalsIgnoreCase(forwardedSsl)) return true;

        String frontEndHttps = request.getHeader("Front-End-Https");
        return "on".equalsIgnoreCase(frontEndHttps);
    }

    private String resolveClientIp(HttpServletRequest request) {
        if (request == null) return "";
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) return realIp.trim();
        return request.getRemoteAddr();
    }

    private String resolveUserAgent(HttpServletRequest request) {
        if (request == null) return "";
        String userAgent = request.getHeader("User-Agent");
        return userAgent == null ? "" : userAgent;
    }

    private String createSignedToken(Map<String, Object> payload) {
        try {
            Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");
            String headerEncoded = encodeBase64Url(objectMapper.writeValueAsBytes(header));
            String payloadEncoded = encodeBase64Url(objectMapper.writeValueAsBytes(payload));
            String toSign = headerEncoded + "." + payloadEncoded;
            String signature = encodeBase64Url(Hashing.hmacSha256(jwtSecret.getBytes(StandardCharsets.UTF_8)).hashString(toSign, StandardCharsets.UTF_8).asBytes());
            return toSign + "." + signature;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Не удалось создать JWT-токен", e);
        }
    }

    private String encodeBase64Url(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }
}
