package com.melly.service.session.service;

import com.melly.service.session.dto.LoginRequestDto;
import com.melly.service.session.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final AuthenticationManager authenticationManager;

    public LoginResponseDto login(LoginRequestDto request, HttpServletRequest httpRequest) {
        try {
            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(authToken);

            // SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션 생성 및 SecurityContext 저장
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            // 권한 가져오기
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(auth -> auth.getAuthority())
                    .orElse(null);

            return LoginResponseDto.builder()
                    .username(authentication.getName())
                    .role(role)
                    .message("로그인 성공")
                    .success(true)
                    .build();

        } catch (Exception e) {
            return LoginResponseDto.builder()
                    .username(null)
                    .role(null)
                    .message("로그인 실패: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }
}
