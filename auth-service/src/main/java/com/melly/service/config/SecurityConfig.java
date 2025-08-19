package com.melly.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melly.domain.repository.UserRepository;
import com.melly.service.auth.CustomAuthenticationProvider;
import com.melly.service.auth.CustomOAuth2UserService;
import com.melly.service.auth.PrincipalDetails;
import com.melly.service.session.dto.SocialLoginResponseDto;
import com.melly.service.session.service.SocialSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final SocialSessionService socialSessionService;

    @Bean
    @Order(1)
    public SecurityFilterChain sessionFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // CSRF는 REST API라면 보통 disable
                .sessionManagement(session -> session   // 세션 사용 (항상 세션을 쓰는 방식)
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .formLogin(form -> form.disable())  // formLogin, httpBasic 은 disable (우린 직접 컨트롤러에서 처리함)
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/users",
                                "/api/v1/auth/session/login",
                                "/api/v1/auth/session/logout",
                                "/swagger-ui/**",        // Swagger UI 접속 허용
                                "/v3/api-docs/**",       // OpenAPI 명세서 JSON 접근 허용
                                "/swagger-ui.html"       // Swagger UI 기본 html 파일
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
                            SocialLoginResponseDto dto = socialSessionService.createSession(principal, request);

                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
                        })
                );
        return http.build();
    }

    // AuthenticationManager Bean 등록 (AuthenticationProvider 사용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userRepository, passwordEncoder);
    }
}
