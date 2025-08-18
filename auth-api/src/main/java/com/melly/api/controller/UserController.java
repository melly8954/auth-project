package com.melly.api.controller;

import com.melly.common.controller.ResponseController;
import com.melly.common.dto.ResponseDto;
import com.melly.service.signup.dto.SignupRequestDto;
import com.melly.service.signup.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements ResponseController {
    private final SignupService signupService;

    @PostMapping("")
    public ResponseEntity<ResponseDto> signup(@RequestBody SignupRequestDto dto) {
        signupService.signup(dto);
        return makeResponseEntity(HttpStatus.OK, null, "회원가입 성공", null);
    }
}
