package me.songha.tutorial.account.exception;

import lombok.Getter;
import me.songha.tutorial.account.domain.Email;

@Getter
public class EmailDuplicationException extends RuntimeException {
    private Email email;
    private String field;

    public EmailDuplicationException(Email email) {
        this.field = "email";
        this.email = email;
    }
}
