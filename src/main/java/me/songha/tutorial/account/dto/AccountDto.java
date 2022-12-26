package me.songha.tutorial.account.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.songha.tutorial.account.domain.Account;
import me.songha.tutorial.account.domain.Address;
import me.songha.tutorial.account.domain.Email;
import me.songha.tutorial.account.domain.Password;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class AccountDto {

    /**
     * 회원가입 요청용 dto inner class
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReq {

        @Valid
        private Email email;
        @NotEmpty
        private String fistName;
        @NotEmpty
        private String lastName;

        private String password;

        @Valid
        private Address address;

        @Builder
        public SignUpReq(Email email, String fistName, String lastName, String password, Address address) {
            this.email = email;
            this.fistName = fistName;
            this.lastName = lastName;
            this.password = password;
            this.address = address;
        }

        public Account toEntity() {
            return Account.builder()
                    .email(this.email)
                    .firstName(this.fistName)
                    .lastName(this.lastName)
                    .password(Password.builder().value(this.password).build())
                    .address(this.address)
                    .build();
        }

    }

    /**
     * 회원 정보 수정 페이지 등에서 수정이 가능한 항목 데이터만을 추려내어 inner class 로 분리하였다.
     * MyAccountReq 클래스에는 3개의 필드가 있으니 오직 3개의 필드만 변경이 가능하다는 것이 아주 명확해진다.
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyAccountReq {
        private Address address;

        @Builder
        public MyAccountReq(final Address address) {
            this.address = address;
        }

    }

    /**
     * 조회의 결과물을 담은 inner class 로 필요한 정보만 노출할 수 있도록 분리한다.
     */
    @Getter
    public static class Res {
        private Email email;
        private Password password;
        private String fistName;
        private String lastName;
        private Address address;

        public Res(Account account) {
            this.email = account.getEmail();
            this.fistName = account.getFirstName();
            this.lastName = account.getLastName();
            this.address = account.getAddress();
            this.password = account.getPassword();
        }
    }
}
