package com.example.testlinux.security.conf;

import com.example.testlinux.service.AuthSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Set;

import static com.example.testlinux.security.conf.AuthService.AUTH_TOKEN_COOKIE_NAME;
import static com.example.testlinux.security.conf.AuthService.AUTH_TOKEN_COOKIE_OLD_NAME;

@Slf4j
@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthSessionService authSessionService;

    private static final String AUTH_TOKEN_HEADER_NAME = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String token = extractTokenFromCookies(httpRequest);

        boolean containsBearer = false;
        if(token == null) {
            token = extractTokenFromHeader(httpRequest);
            containsBearer = true;
        }

        log.debug("filter - uri: " + httpRequest.getRequestURI());
        log.debug("token: " + token);

        if (token != null && !token.isEmpty()) {
            String rawToken = stripBearerPrefix(token);
            String authenticationToken = token;
            JwtToken jwtToken = authService.parseJwtToken(token, containsBearer);
            Auth auth = authService.parseAuth(jwtToken);

            if (auth.isAuthenticated()) {
                if (jwtToken.mustBeSwitchedToOriginal()) {
                    AuthSessionService.SessionTokenChange restoredSession = authSessionService.restoreOriginalSession(rawToken, httpRequest);
                    if (restoredSession == null) {
                        SecurityContextHolder.clearContext();
                        chain.doFilter(request, response);
                        return;
                    }
                    if (response instanceof HttpServletResponse httpResponse) {
                        applyCookies(httpResponse, restoredSession.cookies());
                    }
                    authenticationToken = restoredSession.token();
                    auth = new Auth(restoredSession.userId(), restoredSession.role(), true);
                }

                Set<SimpleGrantedAuthority> grantedAuthorities = Set.of(new SimpleGrantedAuthority(auth.getRole().name()));
                Authentication authentication = new RememberMeAuthenticationToken(authenticationToken, auth, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }



    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    log.debug("Cookie token found: " + AUTH_TOKEN_COOKIE_NAME);
                    return cookie.getValue();
                }
            }

            for (Cookie cookie : cookies) {
                if (AUTH_TOKEN_COOKIE_OLD_NAME.equals(cookie.getName())) {
                    log.debug("Old Cookie token found: " + AUTH_TOKEN_COOKIE_OLD_NAME);
                    return cookie.getValue();
                }
            }
        }

        log.debug("Cookie token NOT found: " + AUTH_TOKEN_COOKIE_NAME);

        return null;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if(token != null && !token.isEmpty()) {
            log.info("Header token found");
        }
        return token;
    }

    private String stripBearerPrefix(String token) {
        if (token == null) {
            return "";
        }
        String trimmed = token.trim();
        if (trimmed.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return trimmed.substring(7).trim();
        }
        return trimmed;
    }

    private void applyCookies(HttpServletResponse response, Iterable<ResponseCookie> cookies) {
        for (ResponseCookie cookie : cookies) {
            response.addHeader("Set-Cookie", cookie.toString());
        }
    }

}
