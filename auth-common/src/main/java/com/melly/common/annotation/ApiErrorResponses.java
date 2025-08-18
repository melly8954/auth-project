package com.melly.common.annotation;

import com.melly.common.exception.ErrorType;
import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiErrorResponses {
    ErrorType[] value() default {}; // 등록할 ErrorType 배열
}
