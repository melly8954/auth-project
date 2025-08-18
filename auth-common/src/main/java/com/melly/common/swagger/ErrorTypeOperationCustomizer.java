package com.melly.common.swagger;

import com.melly.common.annotation.ApiErrorResponses;
import com.melly.common.dto.ResponseDto;
import com.melly.common.exception.ErrorType;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;


@Component
public class ErrorTypeOperationCustomizer implements OperationCustomizer {

    @Override
    public io.swagger.v3.oas.models.Operation customize(io.swagger.v3.oas.models.Operation operation,
                                                        HandlerMethod handlerMethod) {
        ApiErrorResponses annotation = handlerMethod.getMethodAnnotation(ApiErrorResponses.class);

        if (annotation != null) {
            for (ErrorType errorType : annotation.value()) {
                // 예제 객체 생성
                ResponseDto example = new ResponseDto(
                        errorType.getStatus().value(),
                        errorType.name(),
                        errorType.getMessage(),
                        null
                );

                ApiResponse apiResponse = new ApiResponse()
                        .description(errorType.getMessage())
                        .content(new Content().addMediaType(
                                "application/json",
                                new MediaType()
                                        .example(example)
                        ));

                operation.getResponses().addApiResponse(
                        String.valueOf(errorType.getStatus().value()),
                        apiResponse
                );
            }
        }
        return operation;
    }
}
