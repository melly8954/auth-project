package com.melly.api.controller;

import com.melly.common.annotation.ApiErrorResponses;
import com.melly.common.controller.ResponseController;
import com.melly.common.dto.ResponseDto;
import com.melly.common.exception.ErrorType;
import com.melly.service.signup.dto.SignupRequestDto;
import com.melly.service.signup.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User API", description = "사용자 회원관련 API")
public class UserController implements ResponseController {
    private final SignupService signupService;

    @PostMapping("")
    @Operation(summary = "회원가입 API", description = "회원가입 요청을 받아 신규 사용자를 등록합니다.")
    @ApiResponses({
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
                  "message": "회원가입 성공",
                  "data": null
                }
                """
                            )
                    )
            )
    })
    @ApiErrorResponses({
            ErrorType.DUPLICATE_USERNAME, ErrorType.DUPLICATE_EMAIL, ErrorType.PASSWORD_MISMATCH, ErrorType.INTERNAL_ERROR
    })
    public ResponseEntity<ResponseDto> signup(@RequestBody SignupRequestDto dto) {
        signupService.signup(dto);
        return makeResponseEntity(HttpStatus.OK, null, "회원가입 성공", null);
    }
}
