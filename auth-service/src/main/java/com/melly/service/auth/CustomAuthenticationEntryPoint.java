package com.melly.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melly.common.dto.ResponseDto;
import com.melly.common.exception.ErrorType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseDto dto = new ResponseDto(401, ErrorType.UNAUTHORIZED.getErrorCode(), ErrorType.UNAUTHORIZED.getMessage(), null);
        response.getWriter().write(objectMapper.writeValueAsString(dto));
    }
}
