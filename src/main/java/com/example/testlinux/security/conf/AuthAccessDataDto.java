package com.example.testlinux.security.conf;

public record AuthAccessDataDto(String token, String id, String acstatus, String email, String phone,
                                AuthPendingActionDto pendingAction, String passwordResetToken) {}
