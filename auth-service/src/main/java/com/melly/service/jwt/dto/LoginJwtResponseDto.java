package com.melly.service.jwt.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginJwtResponseDto {
    private String username;
    private String token;
    private String role;
    private String message;
    private boolean success;
}
