package me.songha.tutorial.account.service;

import lombok.RequiredArgsConstructor;
import me.songha.tutorial.account.domain.Account;
import me.songha.tutorial.account.domain.Email;
import me.songha.tutorial.account.dto.AccountDto;
import me.songha.tutorial.account.exception.AccountNotFoundException;
import me.songha.tutorial.account.exception.EmailDuplicationException;
import me.songha.tutorial.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account findById(long id) {
        final Optional<Account> account = accountRepository.findById(id);
        account.orElseThrow(() -> new AccountNotFoundException(id));
        return account.get();
    }

    @Transactional(readOnly = true)
    public Account findByEmail(final Email email) {
        final Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new AccountNotFoundException(email);
        }
        return account;
    }

    @Transactional(readOnly = true)
    public boolean isExistedEmail(Email email) {
        return accountRepository.findByEmail(email) != null;
    }

    public Account updateMyAccount(long id, AccountDto.MyAccountReq dto) {
        final Account account = findById(id);
        account.updateMyAccount(dto);
        return account;
    }

    public Account create(AccountDto.SignUpReq dto) {
        if (isExistedEmail(dto.getEmail())) {
            throw new EmailDuplicationException(dto.getEmail());
        }
        return accountRepository.save(dto.toEntity());
    }

}
