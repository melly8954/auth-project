package com.melly.service.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    private String username;
    private String password;
}
