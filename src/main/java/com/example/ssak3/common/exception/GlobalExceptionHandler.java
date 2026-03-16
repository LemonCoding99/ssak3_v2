package com.example.ssak3.common.exception;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> customException(CustomException e) {

        log.error("EXCEPTION: ", e);

        ErrorCode errorCode = e.getErrorCode();

        ApiResponse response = ApiResponse.error(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("EXCEPTION: ", e);

        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ApiResponse response = ApiResponse.error(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> HttpMessageNotReadableException(HttpMessageNotReadableException e) {

        log.error("EXCEPTION: ", e);

        ApiResponse response = ApiResponse.error("올바르지 않은 타입입니다.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        log.error("EXCEPTION: ", e);

        ApiResponse response = ApiResponse.error("올바르지 않은 타입입니다.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> internalServerException(Exception e) {

        log.error("EXCEPTION: ", e);

        ApiResponse response = ApiResponse.error("내부 서버 에러입니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
