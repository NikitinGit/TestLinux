package com.example.testlinux.security.conf;

import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
public class AuthResponseWithCookie {
    private AuthResponse authResponse;
    private ResponseCookie cookie;
}

