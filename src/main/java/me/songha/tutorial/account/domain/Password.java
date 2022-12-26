package me.songha.tutorial.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * [[[ 비밀번호 요구사항 ]]]
 * 1. 비밀번호 만료 기본 30일 기간이 있다.
 * 2. 비밀번호 만료 기간이 지나는 것을 알 수 있어야 한다.
 * 3. 비밀번호 5회 이상 실패했을 경우 더 이상 시도를 못하게 해야 한다.
 * 4. 비밀번호가 일치하는 경우 실패 카운트를 초기화 해야한다.
 * 5. 비밀번호 변경시 만료일이 현재시간 기준 30일로 연장되어야한다.
 */

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    @Column(name = "password", nullable = false)
    private String value;

    @Column(name = "password_expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "password_failed_count", nullable = false)
    private int failedCount;

    @Column(name = "password_ttl")
    private long ttl;

    @Builder
    public Password(final String value) {
        /**
         * JAVA7 이후 버전부터 _는 숫자 리터럴의 어디에도 등장할 수 있다.
         * 2_592_000 는 2592000초, 30일을 의미한다.
         */
        this.ttl = 2_592_000;
        this.value = encodePassword(value);
        this.expirationDate = extendExpirationDate();
    }

    private LocalDateTime extendExpirationDate() {
        return LocalDateTime.now().plusSeconds(ttl);
    }

    private String encodePassword(final String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
