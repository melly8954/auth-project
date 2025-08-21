package com.melly.service.session.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class SocialLoginResponseDto {
    private String username;
    private String role;
    private String message;
    private boolean success;
    private String provider; // GOOGLE, KAKAO 등
    private String providerUserId; // OAuth2 유저 고유 ID
}
