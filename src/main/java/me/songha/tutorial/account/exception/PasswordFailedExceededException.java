package me.songha.tutorial.account.exception;

import lombok.Getter;
import me.songha.tutorial.error.ErrorCode;

@Getter
public class PasswordFailedExceededException extends RuntimeException {
    private ErrorCode errorCode;

    public PasswordFailedExceededException() {
        this.errorCode = ErrorCode.PASSWORD_FAILED_EXCEEDED;
    }
}
