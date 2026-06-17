package com.example.testlinux.security.conf;

import com.example.testlinux.repository.UserTokenRepository;
import com.example.testlinux.service.UserTokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserTokenPresenceService {

    private final UserTokenRepository userTokenRepository;
    private final UserTokenStoreService userTokenStoreService;

    @Transactional
    public boolean touchByRawToken(Integer userId, String rawToken) {
        if (userId == null || rawToken == null || rawToken.isBlank()) {
            return false;
        }
        int updated = userTokenRepository.touchToken(
                userId, userTokenStoreService.sha256Hex(rawToken), LocalDateTime.now());
        return updated > 0;
    }
}
