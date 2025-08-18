package com.melly.service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain sessionFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF는 REST API라면 보통 disable
                .csrf(csrf -> csrf.disable())

                // 세션 사용 (항상 세션을 쓰는 방식)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )

                // 요청별 인가 정책
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users").permitAll()
                        .requestMatchers("/api/v1/auth/session/login").permitAll()
                        .requestMatchers("/api/v1/auth/session/logout").permitAll()
                        .anyRequest().authenticated()
                )

                // formLogin, httpBasic 은 disable (우린 직접 컨트롤러에서 처리함)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // AuthenticationManager Bean 등록 (AuthenticationProvider 사용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
