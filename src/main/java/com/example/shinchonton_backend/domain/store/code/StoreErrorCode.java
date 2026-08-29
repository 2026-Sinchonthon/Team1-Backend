package com.example.shinchonton_backend.domain.store.code;

import com.example.shinchonton_backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {

    STORE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "STORE404_1",
            "가게를 찾을 수 없습니다."
    ),

    STORE_ACCESS_DENIED(
            HttpStatus.FORBIDDEN,
            "STORE403_1",
            "해당 가게를 관리할 권한이 없습니다."
    ),

    MERCHANT_REQUIRED(
            HttpStatus.FORBIDDEN,
            "STORE403_2",
            "사장님 회원만 가게를 등록할 수 있습니다."
    ),

    DUPLICATE_MENU_NAME(
            HttpStatus.CONFLICT,
            "STORE409_1",
            "중복된 메뉴 이름이 존재합니다."
    ),

    MENU_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "STORE404_2",
            "메뉴를 찾을 수 없습니다."
    );

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}