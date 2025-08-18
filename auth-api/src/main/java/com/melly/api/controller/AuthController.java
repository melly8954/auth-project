package com.melly.api.controller;

import com.melly.common.controller.ResponseController;
import com.melly.common.dto.ResponseDto;
import com.melly.service.session.dto.LoginRequestDto;
import com.melly.service.session.dto.LoginResponseDto;
import com.melly.service.session.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements ResponseController {
    private final SessionService sessionService;

    @PostMapping("/session/login")
    public ResponseEntity<ResponseDto> sessionLogin(@RequestBody LoginRequestDto dto, HttpServletRequest request){
        LoginResponseDto response = sessionService.login(dto, request);
        return makeResponseEntity(
                HttpStatus.OK,
                response.isSuccess() ? null : "AUTH_FAIL",
                response.getMessage(),
                response
        );
    }
}
