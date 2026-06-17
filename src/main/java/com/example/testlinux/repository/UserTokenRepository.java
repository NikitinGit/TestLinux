package com.example.testlinux.repository;

import com.example.testlinux.domain.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    @Query(value = "SELECT * FROM user_tokens WHERE site_user_id = :userId AND token = :token ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<UserToken> findLatestBySiteUserIdAndToken(@Param("userId") Integer userId, @Param("token") String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserToken ut WHERE ut.siteUserId = :userId AND ut.token = :token")
    void deleteBySiteUserIdAndToken(@Param("userId") Integer userId, @Param("token") String token);

    @Modifying
    @Query("UPDATE UserToken ut SET ut.lastPresenceOnSiteAt = :now " +
            "WHERE ut.siteUserId = :userId AND ut.token = :token " +
            "AND (ut.expiredAt IS NULL OR ut.expiredAt > :now)")
    int touchToken(@Param("userId") Integer userId, @Param("token") String token, @Param("now") LocalDateTime now);
}