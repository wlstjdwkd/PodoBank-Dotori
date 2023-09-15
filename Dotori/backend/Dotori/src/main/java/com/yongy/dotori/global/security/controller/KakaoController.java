package com.yongy.dotori.global.security.controller;

import com.yongy.dotori.global.security.service.KakaoService;
import com.yongy.dotori.global.common.BaseResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoController {
    private final KakaoService kakaoService;

    // TODO : 인가코드 받기
    @GetMapping("/callback")
    public ResponseEntity<? extends BaseResponseBody> callback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        if(code == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "인증 코드를 가져오는데 실패했습니다."));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, code));
        }
    }

    // TODO : 새로운 accessToken, refreshToken을 발급하기
    @PostMapping("/new-tokens")
    public ResponseEntity<? extends BaseResponseBody> newTokens(@RequestParam String code){
        return kakaoService.newTokens(code);
    }

    // TODO : accessToken으로 사용자 정보 가져오기
    @PostMapping("/userInfo")
    public ResponseEntity<? extends BaseResponseBody> getUserInfo(@RequestParam String accessToken) throws Exception {
        return kakaoService.getUserInfo(accessToken);
    }


    // TODO : 토큰의 유효성 검사
    // accessToken이 유효한지 검사한 후 유효하면 200을 반환함
    @PostMapping("/token-valid")
    public ResponseEntity<? extends BaseResponseBody> tokenValid(@RequestParam String accessToken){
        return kakaoService.validateToken(accessToken);
    }


    // TODO : 토큰 갱신하기 (FE랑 상의하기)
    // accessToken과 refreshToken을 갱신한다.
    @PostMapping("/token-update")
    public ResponseEntity<? extends BaseResponseBody> tokenUpdate(@RequestParam String id){
        return kakaoService.tokenUpdate(id);
    }

}