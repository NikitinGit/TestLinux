package com.example.testlinux.security.conf;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class LoggingAccessDeniedHandler implements AccessDeniedHandler {

    @PostConstruct
    public void init() {
        log.info("Custom AccessDeniedHandler active.");
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";

        log.warn("Access denied for user [{}] on [{}] – reason: {}",
                username,
                request.getRequestURI(),
                accessDeniedException.getMessage());

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
