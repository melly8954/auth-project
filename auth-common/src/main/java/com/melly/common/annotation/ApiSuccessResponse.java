package com.melly.common.annotation;

import com.melly.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Target({ElementType.METHOD})  // 메서드에만 적용
@Retention(RetentionPolicy.RUNTIME)  // 런타임까지 유지
@Documented  // javadoc에 포함
@ApiResponses({  // 붙은 메서드에 자동으로 이 ApiResponses 적용
        @ApiResponse(
                responseCode = "200",
                description = "API 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ResponseDto.class))
        )
})
public @interface ApiSuccessResponse {
}
