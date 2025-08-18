package com.melly.service.signup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "회원가입 요청 DTO")
public class SignupRequestDto {
    @Schema(description = "사용자 ID", example = "testid01")
    private String username;

    @Schema(description = "비밀번호", example = "1q2w3e4r!")
    private String password;

    @Schema(description = "비밀번호 확인", example = "1q2w3e4r!")
    private String confirmPassword;

    @Schema(description = "이메일", example = "testid01@example.com")
    private String email;
}
