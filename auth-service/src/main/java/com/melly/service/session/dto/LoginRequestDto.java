package com.melly.service.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    @Schema(description = "사용자 ID", example = "testid01")
    private String username;

    @Schema(description = "비밀번호", example = "1q2w3e4r!")
    private String password;
}
