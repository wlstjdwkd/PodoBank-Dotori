package com.yongy.dotorimainservice.domain.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotorimainservice.domain.account.dto.AccountDTO;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountNumberReqDto;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountNumberTitleReqDto;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountReqDto;
import com.yongy.dotorimainservice.domain.account.dto.communication.UserReqDto;
import com.yongy.dotorimainservice.domain.account.entity.Account;
import com.yongy.dotorimainservice.domain.account.service.AccountService;
import com.yongy.dotorimainservice.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "전체 계좌 조회")
    @GetMapping()
    public ResponseEntity<List<AccountDTO>> findAllAccount() throws JsonProcessingException {
        List<AccountDTO> result = accountService.findAllAccount();
        return ResponseEntity.ok(result);
    }

    // NOTE : 사용자의 계좌 1개를 삭제함
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUserOneAccount(@RequestParam Long accountSeq){
        accountService.removeUserAccount(accountSeq);
        return ResponseEntity.ok().build();
    }

    // ------- 통신 --------
    // NOTE : 사용자의 계좌 전체를 삭제한다.
    @PostMapping("/communication/delete/all")
    public ResponseEntity<String> deleteUserAccount(@RequestBody UserReqDto userReqDto){
        accountService.removeUserAllAccounts(userReqDto.getUserSeq());
        return ResponseEntity.ok().build();
    }

    // NOTE : 사용자의 계좌가 존재하는 확인한다.
    @PostMapping("/communication")
    public ResponseEntity<String> accountVisibleCheck(@RequestBody AccountNumberReqDto accountNumberReqDto){
        log.info("come come");
        Account account = accountService.getUserAccount(accountNumberReqDto.getAccountNumber());
        if(account == null)
            return ResponseEntity.ok("NO"); // 존재하지않음
        return ResponseEntity.ok("YES"); // 존재함
    }



    // NOTE : Account 객체를 저장한다.
    @PostMapping("/communication/save")
    public ResponseEntity<String> saveAccount(@RequestBody AccountReqDto accountReqDto){
        accountService.saveAccount(accountReqDto);
        return ResponseEntity.ok().build();
    }

    // NOTE : 사용자의 계좌 이름을 설정한다.
    @PostMapping("/communication/setTitle")
    public ResponseEntity<String> setAccountName(@RequestBody AccountNumberTitleReqDto accountNumberTitleReqDto){
        accountService.saveAccountTitle(accountNumberTitleReqDto);
        return ResponseEntity.ok().build();
    }


}
