package com.example.testlinux.config;

import com.example.testlinux.security.conf.Auth;
import com.example.testlinux.security.conf.LoggingAccessDeniedHandler;
import com.example.testlinux.security.conf.TokenAuthenticationFilter;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // включает @PreAuthorize/@PostAuthorize; без него аннотации молча игнорируются
public class SpringSecurityConfig {

 /*   @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        System.out.println("inMemoryUserDetailsManager()");
        String generatedPassword = "nikitin";
        return new InMemoryUserDetailsManager(User.withUsername("user")
                .password(generatedPassword).roles("USER").build());
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher(ApplicationEventPublisher delegate) {
        System.out.println("defaultAuthenticationEventPublisher()");
        return new DefaultAuthenticationEventPublisher(delegate);
    }*/

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public AccessDeniedHandler loggingAccessDeniedHandler() {
        return new LoggingAccessDeniedHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);

        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));

        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*'
        // when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);

        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "x-xsrf-token", "Cache-Control", "Content-Type", "Access-Control-Allow-Headers", "access-control-expose-headers"));
        configuration.setExposedHeaders(ImmutableList.of("Authorization", "Content-Disposition"));


        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))

//                .authorizeRequests(request ->
//                        request.requestMatchers(ENDPOINTS_WHITELIST).permitAll().anyRequest().authenticated())
//                request.anyRequest().permitAll())

//        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

                .exceptionHandling(e -> e.accessDeniedHandler(loggingAccessDeniedHandler()))
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/event/**").hasAuthority(Auth.Role.Organizer.name())
                                // Websockets
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/ws-public/**").permitAll()
                                .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/organizers/access-list").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/organizers/*/access").authenticated()
                                .requestMatchers("/api/events/**").permitAll()
                                .requestMatchers("/api/organizer-judge/**").hasAuthority(Auth.Role.Judge.name())
                                // Управление банами комментаторов: доступно аутентифицированным с разрешением comment_ban_manage
                                .requestMatchers("/api/comment-bans/**").authenticated()
                );

        return http.build();
    }
}
