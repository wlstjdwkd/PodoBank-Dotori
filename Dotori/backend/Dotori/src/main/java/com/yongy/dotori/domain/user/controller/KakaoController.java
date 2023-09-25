package com.yongy.dotori.domain.user.controller;

import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.FailedSocialAuthException;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.user.service.UserService;
import com.yongy.dotori.global.redis.entity.UserRefreshToken;
import com.yongy.dotori.global.redis.repository.UserRefreshTokenRepository;
import com.yongy.dotori.global.security.dto.JwtToken;
import com.yongy.dotori.global.security.provider.JwtTokenProvider;
import com.yongy.dotori.domain.user.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/kakao")
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "카카오 회원가입 성공"),
            @ApiResponse(responseCode = "201", description = "카카오 로그인 성공"),
            @ApiResponse(responseCode = "403", description = "카카오 인증에 실패했습니다.")
    })
    @Operation(summary = "카카오 회원가입/로그인", description = "ALL")
    // NOTE : 인가코드 받기
    @GetMapping("/callback")
    public ResponseEntity<JwtToken> callback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");

        // NOTE : 인가코드 인증 실패
        if(code == null){
            throw new FailedSocialAuthException("카카오 인증에 실패했습니다.");
        }

        String accessToken = kakaoService.getAccessToken(code);

        // NOTE : AccessToken 인증 실패
        if(accessToken == null){
            throw new FailedSocialAuthException("카카오 인증에 실패했습니다.");
        }

        User user = kakaoService.getUserInfo(accessToken);

        // NOTE : 사용자 정보 가져오기 실패
        if(user == null){
            throw new FailedSocialAuthException("카카오 인증에 실패했습니다.");
        }else{
            // NOTE : 회원가입

            if(userService.getUser(user.getId()) == null){
                userService.saveUser(user); // DB에 사용자 저장
                return ResponseEntity.ok().build();
            }

            // NOTE : 로그인
            JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), user.getRole());

            userService.saveUserRefreshToken(UserRefreshToken.of(user.getId(), jwtToken.getRefreshToken()));

            return ResponseEntity.ok().body(jwtToken);
        }
    }

}