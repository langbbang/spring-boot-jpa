package me.songha.tutorial.error;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * ExceptionHandling 을 통해 뱉은 Spring 기본 에러 response 값에는 필요 이상의 정보들이 담겨있다.
 * 프론트에서 작업을 하기에 적합하지 않으므로 공통적인 포맷을 가져가는 것이 바람직하다.
 */

@Getter
public class ErrorResponse {
    private String message;
    private String code;
    private int status;
    private List<FieldError> errors = new ArrayList<>();

    @Builder
    public ErrorResponse(String message, String code, int status, List<FieldError> errors) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.errors = initErrors(errors);
    }

    private List<FieldError> initErrors(List<FieldError> errors) {
        return (errors == null) ? new ArrayList<>() : errors;
    }

    @Getter
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }

}
