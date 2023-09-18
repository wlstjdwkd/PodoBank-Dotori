package com.yongy.dotori.global.security.controller;


import com.yongy.dotori.global.security.service.NaverService;
import com.yongy.dotori.global.common.BaseResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/naver")
public class NaverController {

    @Autowired
    private NaverService naverService;

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
    public ResponseEntity<? extends BaseResponseBody> newTokens(@RequestParam String code) throws Exception {
        return naverService.newTokens(code);
    }




}
