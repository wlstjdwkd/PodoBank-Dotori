package com.yongy.dotori.domain.userAuth.controller;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.exception.ExistAccountNumberException;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.user.dto.request.UserEmailReqDto;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.ExpiredAuthCodeException;
import com.yongy.dotori.domain.user.exception.InvalidAuthCodeException;
import com.yongy.dotori.domain.user.exception.InvalidIdException;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountCodeDto;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountDto;
import com.yongy.dotori.domain.userAuth.exception.FailedOneReqException;
import com.yongy.dotori.domain.userAuth.service.UserAuthService;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.redis.repository.BankAccessTokenRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private AccountRepository accountRepository;

    // NOTE : 1원인증 사용자 이메일 인증번호 보내기

    @ApiResponse(responseCode = "404", description = "사용자의 정보의 이메일과 정보가 일치하지 않습니다.")
    @Operation(summary = "[1원 인증의 본인인증] 사용자 이메일에 인증번호 보내기", description = "USER")
    @PostMapping("/own/check-id")
    public ResponseEntity<Void> sendMsgAuthCode(@RequestParam String id){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User)auth.getPrincipal();

        if(authUser.getId().equals(id)){
            // 문자 보내기
            userAuthService.ownCert(id);
            return ResponseEntity.ok().build();
        }
        throw new InvalidIdException("사용자의 정보의 이메일과 정보가 일치하지 않습니다.");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1원인증 완료"),
            @ApiResponse(responseCode = "404", description = "인증번호가 만료되었습니다."),
            @ApiResponse(responseCode = "409", description = "인증번호가 올바르지 않습니다.")
    })
    @Operation(summary = "[1원 인증의 본인인증] 인증코드 확인하기", description = "USER")
    // NOTE : 1원인증 인증코드 확인하기
    @PostMapping("/own/check-code")
    public ResponseEntity<Void>validSmsCodeCheck(@RequestBody UserEmailReqDto userEmailReqDto){
        String authId = userAuthService.getOwnAuthId(userEmailReqDto.getCode());

        if(authId == null) { // 인증번호의 시간이 만료됨
            throw new ExpiredAuthCodeException("인증번호가 만료되었습니다.");
        }else if(authId.equals(userEmailReqDto.getId())){
            userAuthService.deleteOwnAuthCode(userEmailReqDto.getCode()); // 인증번호 삭제
            return ResponseEntity.ok().build();
        }else{ // 인증번호가 틀림
            throw new InvalidAuthCodeException("인증번호가 올바르지 않습니다.");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포도은행에 1원 인증 성공"),
            @ApiResponse(responseCode = "404", description = "포도은행에 1원 인증 실패")
    })
    @Operation(summary = "[포도은행에서 1원 인증] 포도은행에서 계좌의 존재 여부 확인한 후 1원 인증 요청하기", description = "USER")
    // NOTE : 포도은행에서 계좌의 존재 여부 확인하기(토큰 O : 1원인증, 토큰 X : 포도은행에 로그인하고 accessToken, refreshToken 받아온 후 1원인증
    @PostMapping("/podoBank/check-account")
    public ResponseEntity<Void> sendAccountAuthCode(@RequestBody UserAccountDto userAccountDto) {
        String responseCode = userAuthService.sendAccountInfo(userAccountDto);

        // NOTE : 계좌에 데이터가 없으면 호출
        Account account = accountRepository.findByAccountNumberAndDeleteAtIsNull(userAccountDto.getAccountNumber());
        if(account != null){
            throw new ExistAccountNumberException("이미 연결된 계좌입니다.");
        }

        if(responseCode.equals("200"))
            return ResponseEntity.ok().build();
        throw new FailedOneReqException("포도은행에 1원 인증 실패");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드 일치"),
            @ApiResponse(responseCode = "404", description = "1원 인증 실패")
    })
    @Operation(summary = "[포도은행에서 1원 인증] 포도은행에 1원 인증코드 검사하기", description = "USER")
    // NOTE : 인증번호 전송
    @PostMapping("/podoBank/check-code")
    public ResponseEntity<Void> checkAccountAuthCode(@RequestBody UserAccountCodeDto userAccountCodeDto) throws ParseException {
        return userAuthService.checkAccountAuthCode(userAccountCodeDto);
    }

    @ApiResponse(responseCode = "200", description = "계좌이름 설정 완료")
    @Operation(summary = "계좌의 이름 설정하기", description = "USER")
    @PostMapping("/account/title")
    public ResponseEntity<Void> setAccountName(@RequestParam String accountNumber,@RequestParam String accountTitle){
        Account account = accountRepository.findByAccountNumberAndDeleteAtIsNull(accountNumber);
        account.setAccountTitle(accountTitle);
        accountRepository.save(account);
        return ResponseEntity.ok().build();
    }


}
