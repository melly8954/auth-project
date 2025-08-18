package com.melly.service.signup.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequestDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
