package com.melly.service.jwt;

import com.melly.domain.entity.UserEntity;
import com.melly.domain.repository.UserRepository;
import com.melly.service.auth.PrincipalDetails;
import com.melly.service.jwt.dto.LoginJwtResponseDto;
import com.melly.service.session.dto.LoginRequestDto;
import com.melly.service.session.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public LoginJwtResponseDto login(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        UserEntity user = ((PrincipalDetails) authentication.getPrincipal()).getUserEntity();

        String accessToken = jwtUtil.createJwt("AccessToken", user.getUsername(), user.getRole().name(), 600000L);

        return LoginJwtResponseDto.builder()
                .username(user.getUsername())
                .token(accessToken)
                .role(user.getRole().name())
                .message("로그인 성공")
                .build();
    }
}
