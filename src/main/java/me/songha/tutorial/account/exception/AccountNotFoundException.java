package me.songha.tutorial.account.exception;

import me.songha.tutorial.account.domain.Email;
import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {

    private long id;
    private Email email;

    public AccountNotFoundException(long id) {
        this.id = id;
    }

    public AccountNotFoundException(Email email) {
        this.email = email;
    }

}
