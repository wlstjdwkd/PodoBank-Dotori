package com.yongy.dotori.domain;

import com.yongy.dotori.domain.user.entity.User;
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
        String accessToken = kakaoService.newTokens(code);
        if(accessToken == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "유효하지 않는 인증 코드입니다"));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, accessToken));
        }
    }

    // TODO : accessToken으로 사용자 정보 가져오기
    @PostMapping("/userInfo")
    public ResponseEntity<? extends BaseResponseBody> getUserInfo(@RequestParam String accessToken){
        try{
            User user = kakaoService.getUserInfo(accessToken);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, user));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, "유효한 토큰인지 확인하세요"));
        }
    }


    // TODO : 토큰의 유효성 검사
    // accessToken이 유효한지 검사한 후 유효하면 200을 반환함
    @PostMapping("/token-valid")
    public ResponseEntity<? extends BaseResponseBody> tokenValid(@RequestParam String accessToken){
        ResponseEntity<? extends BaseResponseBody> state = kakaoService.validateToken(accessToken);
        return state;
    }


    // TODO : 토큰 갱신하기
    // accessToken과 refreshToken을 갱신한다.
    @PostMapping("/token-update")
    public ResponseEntity<? extends BaseResponseBody> tokenUpdate(@RequestParam String id){
        // refreshToken이 없다? 즉, 존재하지 않으면 인가 코드를 발급받음
        String accessToken = kakaoService.tokenUpdate(id);
        if(accessToken == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "유효하지 않는 인증 코드입니다"));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, accessToken));
        }
    }

}