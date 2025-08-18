package com.melly.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "API 공통 응답")
@JsonInclude(JsonInclude.Include.ALWAYS) // null도 직렬화
public class ResponseDto{

    @Schema(description = "HTTP 상태 코드", example = "200")
    private int code;

    @Schema(description = "에러 코드", example = "null")
    private String errorCode;

    @Schema(description = "응답 메시지", example = "API 성공")
    private String message;

    @Schema(description = "응답 데이터")
    private Object data;
}
