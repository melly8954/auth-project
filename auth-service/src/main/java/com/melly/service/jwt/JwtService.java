package com.melly.service.jwt;

import com.melly.common.exception.CustomException;
import com.melly.common.exception.ErrorType;
import com.melly.domain.entity.UserEntity;
import com.melly.domain.repository.UserRepository;
import com.melly.service.auth.PrincipalDetails;
import com.melly.service.jwt.dto.LoginJwtResponseDto;
import com.melly.service.session.dto.LoginRequestDto;
import com.melly.service.session.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public LoginJwtResponseDto login(LoginRequestDto dto) {
        try {
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
        } catch (BadCredentialsException e) {
            throw new CustomException(ErrorType.BAD_CREDENTIALS);
        } catch (DisabledException e) {
            if ("USER_DELETED".equals(e.getMessage())) {
                throw new CustomException(ErrorType.USER_DELETED);
            }
            throw new CustomException(ErrorType.USER_INACTIVE);
        } catch (AuthenticationException e) {
            throw new CustomException(ErrorType.INTERNAL_ERROR);
        }
    }
}
