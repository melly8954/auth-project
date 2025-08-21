package com.melly.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melly.domain.repository.UserRepository;
import com.melly.service.auth.CustomAuthenticationEntryPoint;
import com.melly.service.auth.CustomAuthenticationProvider;
import com.melly.service.auth.CustomOAuth2UserService;
import com.melly.service.auth.PrincipalDetails;
import com.melly.service.jwt.JwtFilter;
import com.melly.service.jwt.JwtUtil;
import com.melly.service.session.dto.SocialLoginResponseDto;
import com.melly.service.session.service.SessionSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final SessionSocialService sessionSocialService;
    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * 인증 불필요한 공개 API 체인
     */
    @Bean
    @Order(0)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/api/v1/users",          // 회원가입
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/", "/css/**", "/js/**", "/images/**" // 정적 리소스
                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    /**
     * 세션 기반 인증 체인
     */
    @Bean
    @Order(1)
    public SecurityFilterChain sessionFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/auth/session/**","/oauth2/**","/login/oauth2/**")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/session/login",
                                "/api/v1/auth/session/logout",
                                "/login/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                            String registrationId = oauthToken.getAuthorizedClientRegistrationId(); // google, kakao 등

                            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
                            SocialLoginResponseDto dto = sessionSocialService.createSession(principal, request, registrationId);

                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
                        })
                );
        return http.build();
    }

    /**
     * JWT 기반 인증 체인
     */
    @Bean
    @Order(2)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/auth/jwt/**", "/api/v1/users/test")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/jwt/login",
                                "/api/v1/auth/jwt/logout")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                // JwtFilter 를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(new JwtFilter(jwtUtil,userRepository), UsernamePasswordAuthenticationFilter.class);
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
