package com.miniproject.config.Error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "정상적이지 않은 토큰입니다."),
    BAD_CREDENTIAL(HttpStatus.BAD_REQUEST, "잘못된 로그인 정보입니다."),
    NOT_VALID_ACCOUNT(HttpStatus.UNAUTHORIZED, "이메일 인증이 완료되지 않은 계정입니다."),

    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
