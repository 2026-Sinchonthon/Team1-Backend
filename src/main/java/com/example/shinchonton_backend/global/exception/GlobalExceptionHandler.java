package com.example.shinchonton_backend.global.exception;

import com.example.shinchonton_backend.global.apiPayload.ApiResponse;
import com.example.shinchonton_backend.global.apiPayload.code.BaseErrorCode;
import com.example.shinchonton_backend.global.apiPayload.code.status.GeneralErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(
            GeneralException e
    ) {
        BaseErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.onFailure(errorCode, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        BaseErrorCode code = GeneralErrorCode.BAD_REQUEST;

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ApiResponse.onFailure(code, errors));
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            HandlerMethodValidationException.class,
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception e) {

        BaseErrorCode code = GeneralErrorCode.BAD_REQUEST;

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ApiResponse.onFailure(code, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(
            Exception e
    ) {
        BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ApiResponse.onFailure(code, e.getMessage()));
    }
}