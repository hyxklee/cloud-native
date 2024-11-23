package com.example.demo.global.config.exception;

import com.example.demo.global.config.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class : {}, Message : {}";

    @ExceptionHandler(RuntimeException.class)  // 커스텀 예외 처리
    public CommonResponse<Void> handle(RuntimeException ex) {
        log.warn("구체로그: ", ex);
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), ex.getMessage());

        CommonResponse<Void> response = CommonResponse.createFailure(500, ex.getMessage());

        return response;
    }
}
