package com.example.shinchonton_backend.global.exception;

import com.example.shinchonton_backend.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{
    private final BaseErrorCode errorCode;
    public GeneralException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
