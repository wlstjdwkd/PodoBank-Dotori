package com.yongy.dotori.domain.userAuth.controller;

import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.ExceptionEnum;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountCodeDto;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountDto;
import com.yongy.dotori.domain.userAuth.service.UserAuthService;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.redis.repository.DotoriAccessTokenRepository;
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
    private DotoriAccessTokenRepository dotoriAccessTokenRepository;

    // NOTE : 1원인증 사용자 이메일 인증번호 보내기
    @PostMapping("/own/check-id")
    public ResponseEntity<? extends BaseResponseBody> sendMsgAuthCode(@RequestParam String id){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User)auth.getPrincipal();

        if(authUser.getId().equals(id)){
            // 문자 보내기
            userAuthService.ownCert(id);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "인증코드 전송 완료"));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "사용자의 정보의 이메일과 정보가 일치하지 않습니다."));
        }
    }

    // NOTE : 1원인증 인증코드 확인하기
    @PostMapping("/own/check-code")
    public ResponseEntity<? extends BaseResponseBody>validSmsCodeCheck(@RequestParam String code){
        String authId = userAuthService.getOwnAuthId(code);

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info(authId+"/"+user.getId());
        if(authId == null) { // 인증번호의 시간이 만료됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4002, ExceptionEnum.EXPIRED_AUTHCODE));
        }else if(authId.equals(user.getId())){
            userAuthService.deleteOwnAuthCode(code); // 인증번호 삭제
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "인증 완료"));
        }else{ // 인증번호가 틀림
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4003, ExceptionEnum.INVALID_AUTHCODE));
        }
    }

    // NOTE : 포도은행에서 계좌의 존재 여부 확인하기(토큰 O : 1원인증, 토큰 X : 포도은행에 로그인하고 accessToken, refreshToken 받아온 후 1원인증
    @PostMapping("/podoBank/check-account")
    public ResponseEntity<? extends BaseResponseBody> sendAccountAuthCode(@RequestBody UserAccountDto userAccountDto) throws ParseException {
        return userAuthService.sendAccountInfo(userAccountDto);
    }

    // NOTE : 인증번호 전송
    @PostMapping("/podoBank/check-code")
    public ResponseEntity<? extends BaseResponseBody> checkAccountAuthCode(@RequestBody UserAccountCodeDto userAccountCodeDto) throws ParseException{
        return userAuthService.checkAccountAuthCode(userAccountCodeDto);
    }

}
