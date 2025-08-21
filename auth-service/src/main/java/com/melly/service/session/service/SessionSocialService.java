package com.melly.service.session.service;

import com.melly.common.exception.CustomException;
import com.melly.common.exception.ErrorType;
import com.melly.domain.entity.UserAuthProviderEntity;
import com.melly.domain.entity.UserEntity;
import com.melly.domain.repository.UserAuthProviderRepository;
import com.melly.service.auth.PrincipalDetails;
import com.melly.service.session.dto.SocialLoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionSocialService {
    private final UserAuthProviderRepository userAuthProviderRepository;

    public SocialLoginResponseDto createSession(PrincipalDetails principal, HttpServletRequest request, String registrationId) {
        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        UserEntity user = principal.getUserEntity();
        UserAuthProviderEntity authProvider = userAuthProviderRepository
                .findByUserIdAndProviderFetchJoin(user.getUserId(), registrationId)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        return SocialLoginResponseDto.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .message("소셜 로그인 성공")
                .success(true)
                .provider(authProvider.getProvider())
                .providerUserId(authProvider.getProviderUserId())
                .build();
    }
}
