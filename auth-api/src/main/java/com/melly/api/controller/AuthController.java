package com.melly.api.controller;

import com.melly.common.annotation.ApiErrorResponses;
import com.melly.common.controller.ResponseController;
import com.melly.common.dto.ResponseDto;
import com.melly.common.exception.ErrorType;
import com.melly.service.jwt.JwtService;
import com.melly.service.jwt.dto.LoginJwtResponseDto;
import com.melly.service.session.dto.LoginRequestDto;
import com.melly.service.session.dto.LoginResponseDto;
import com.melly.service.session.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API", description = "사용자 인증방식 API")
public class AuthController implements ResponseController {
    private final SessionService sessionService;
    private final JwtService jwtService;

    @PostMapping("/session/login")
    @Operation(summary = "세션 로그인", description = "아이디와 비밀번호로 세션 로그인 처리")
    @ApiResponse(
            responseCode = "200",
            description = "회원가입 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                {
                    "code": 200,
                    "errorCode": null,
                    "message": "로그인 성공",
                    "data": {
                        "username": "testid01",
                        "role": "USER",
                        "message": "로그인 성공",
                        "success": true
                    }
                }
                """
                    )
            )
    )
    @ApiErrorResponses({
            ErrorType.BAD_CREDENTIALS, ErrorType.USER_INACTIVE, ErrorType.USER_DELETED, ErrorType.INTERNAL_ERROR
    })
    public ResponseEntity<ResponseDto> sessionLogin(@RequestBody LoginRequestDto dto, HttpServletRequest request){
        LoginResponseDto response = sessionService.login(dto, request);
        return makeResponseEntity(HttpStatus.OK, null, response.getMessage(), response);
    }

    @PostMapping("/session/logout")
    @Operation(summary = "세션 로그아웃", description = "현재 세션 로그아웃 처리")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request, HttpServletResponse response) {
        sessionService.logout(request, response);
        return makeResponseEntity(HttpStatus.OK, null, "로그아웃 성공", null);
    }

    @PostMapping("/jwt/login")
    public ResponseEntity<ResponseDto> jwtLogin(@RequestBody LoginRequestDto dto) {
        LoginJwtResponseDto response = jwtService.login(dto);
        return makeResponseEntity(HttpStatus.OK, null, response.getMessage(), response);
    }

    @GetMapping("/session/test")
    public ResponseEntity<ResponseDto> test1() {
        return makeResponseEntity(HttpStatus.OK, null, "session 테스트 성공", "session ok");
    }

    @GetMapping("/jwt/test")
    public ResponseEntity<ResponseDto> test2() {
        return makeResponseEntity(HttpStatus.OK, null, "jwt 테스트 성공", "jwt ok");
    }
}
