package me.songha.tutorial.account.repository;

import me.songha.tutorial.account.domain.Account;
import me.songha.tutorial.account.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(Email email);

    boolean existsByEmail(Email email);
}
