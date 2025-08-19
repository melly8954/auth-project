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
}
