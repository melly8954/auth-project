package com.melly.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("로그인 관리 프로젝트 API 명세서")
                        .version("v1.0")
                        .description("이 문서는 프로젝트의 REST API 명세서입니다."));
    }
}
