package com.melly.service.auth;

import com.melly.domain.entity.UserEntity;
import com.melly.common.exception.CustomException;
import com.melly.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.melly.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        return new PrincipalDetails(user);
    }
}
