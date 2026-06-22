package com.example.testlinux.security.conf;

import org.springframework.http.ResponseCookie;

import java.util.List;

public record AuthSessionResult(AuthAccessDataDto data, List<ResponseCookie> cookies) {}
