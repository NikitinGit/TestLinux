package com.example.testlinux.service;

import com.example.testlinux.domain.UserToken;
import com.example.testlinux.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class UserTokenStoreService {

    private final UserTokenRepository userTokenRepository;

    public record TokenClientInfo(String userAgent, String ip) {}

    @Transactional
    public void create(Integer userId, String token, Instant expiresAt, String userAgent, String ip) {
        if (userId == null || token == null || token.isBlank() || expiresAt == null) return;

        LocalDateTime now = LocalDateTime.now();
        UserToken userToken = new UserToken();
        userToken.setSiteUserId(userId);
        userToken.setToken(sha256Hex(token));
        userToken.setUserAgent(userAgent == null ? "" : userAgent);
        userToken.setIp(truncateIp(ip));
        userToken.setCreatedAt(now);
        userToken.setExpiredAt(LocalDateTime.ofInstant(expiresAt, ZoneOffset.UTC));
        userToken.setLastPresenceOnSiteAt(now);
        userTokenRepository.save(userToken);
    }

    @Transactional
    public void delete(Integer userId, String token) {
        if (userId == null || token == null || token.isBlank()) return;
        userTokenRepository.deleteBySiteUserIdAndToken(userId, sha256Hex(token));
    }

    public TokenClientInfo findClientInfo(Integer userId, String token) {
        if (userId == null || token == null || token.isBlank()) return null;
        return userTokenRepository.findLatestBySiteUserIdAndToken(userId, sha256Hex(token))
                .map(ut -> new TokenClientInfo(ut.getUserAgent(), ut.getIp()))
                .orElse(null);
    }

    private String truncateIp(String ip) {
        if (ip == null) return "";
        return ip.length() <= 31 ? ip : ip.substring(0, 31);
    }

    public String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}