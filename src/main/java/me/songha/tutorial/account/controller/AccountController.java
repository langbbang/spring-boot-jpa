package me.songha.tutorial.account.controller;

import lombok.RequiredArgsConstructor;
import me.songha.tutorial.account.dto.AccountDto;
import me.songha.tutorial.account.dto.AccountDto.Res;
import me.songha.tutorial.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /**
     * [[[회원 가입]]]
     * <p>
     * HttpStatus.CREATED :: 201 - 성공적으로 요청되었으며 서버가 새 리소스를 작성했다.
     * <p>
     * PostMapping(consumes = ) :: 들어오는 데이터 타입 정의
     * PostMapping(produces = ) :: 반환하는 데이터 타입 정의
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Res signUp(@RequestBody @Valid final AccountDto.SignUpReq dto) {
        return new AccountDto.Res(accountService.create(dto));
    }

    /**
     * 고유 id 값으로 account 조회
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public AccountDto.Res getUser(@PathVariable final long id) {
        return new AccountDto.Res(accountService.findById(id));
    }

    /**
     * 고유 id 값으로 account 의 address 정보 수정
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public AccountDto.Res updateMyAccount(@PathVariable final long id, @RequestBody final AccountDto.MyAccountReq dto) {
        return new AccountDto.Res(accountService.updateMyAccount(id, dto));
    }

}
