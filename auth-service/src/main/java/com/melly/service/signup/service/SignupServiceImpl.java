package com.melly.service.signup.service;

import com.melly.service.signup.dto.SignupRequestDto;
import com.melly.domain.entity.UserEntity;
import com.melly.domain.enums.UserRole;
import com.melly.domain.enums.UserStatus;
import com.melly.common.exception.CustomException;
import com.melly.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.melly.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void signup(SignupRequestDto request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorType.DUPLICATE_USERNAME);
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL);
        }

        if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new CustomException(ErrorType.PASSWORD_MISMATCH);
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);
    }
}
