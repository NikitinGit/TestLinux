package com.example.testlinux.security.conf;

import java.util.Map;

public record AuthPendingActionDto(String action, Map<String, Object> payload) {}
