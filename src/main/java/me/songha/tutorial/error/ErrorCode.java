package me.songha.tutorial.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ACCOUNT_NOT_FOUND("error_001", "해당 회원을 찾을 수 없습니다.", 404),
    EMAIL_DUPLICATION("error_002", "이메일이 중복되었습니다.", 400),
    INPUT_VALUE_INVALID("error_003", "입력값이 올바르지 않습니다.", 400),
    PASSWORD_FAILED_EXCEEDED("error_004", "비밀번호 실패 횟수가 초과했습니다.", 400);

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
