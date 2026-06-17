package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_tokens")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_user_id")
    private Integer siteUserId;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "user_agent", length = 255, nullable = false)
    private String userAgent;

    @Column(name = "ip", length = 31, nullable = false)
    private String ip;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "last_presence_on_site_at")
    private LocalDateTime lastPresenceOnSiteAt;
}
