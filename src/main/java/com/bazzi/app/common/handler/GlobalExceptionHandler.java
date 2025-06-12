package com.bazzi.app.common.handler;

import com.bazzi.app.application.exception.CustomException;
import com.bazzi.app.common.response.ApiResponse;
import com.bazzi.app.common.response.ErrorResponse;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorResponse response = new ErrorResponse(ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    // Redis 연결 오류 처리
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ErrorResponse> handleRedisConnectionFailure(RedisConnectionFailureException ex) {
        ErrorResponse response = new ErrorResponse(500, "Redis 연결에 실패했습니다.");
        return ResponseEntity.status(500).body(response);
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse response = new ErrorResponse(500, "서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(500).body(response);
    }
}
