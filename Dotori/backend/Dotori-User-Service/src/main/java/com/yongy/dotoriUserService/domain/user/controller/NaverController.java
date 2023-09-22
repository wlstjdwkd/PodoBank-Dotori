package com.yongy.dotoriUserService.domain.user.controller;

import com.yongy.dotoriUserService.domain.user.entity.User;
import com.yongy.dotoriUserService.domain.user.exception.FailedSocialAuthException;
import com.yongy.dotoriUserService.domain.user.repository.UserRepository;
import com.yongy.dotoriUserService.domain.user.service.NaverService;
import com.yongy.dotoriUserService.global.redis.entity.RefreshToken;
import com.yongy.dotoriUserService.global.redis.repository.RefreshTokenRepository;
import com.yongy.dotoriUserService.global.security.dto.JwtToken;
import com.yongy.dotoriUserService.global.security.provider.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/naver")
public class NaverController {

    @Autowired
    private NaverService naverService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "네이버 회원가입 성공"),
            @ApiResponse(responseCode = "201", description = "네이버 로그인 성공"),
            @ApiResponse(responseCode = "403", description = "네이버 인증에 실패했습니다.")
    })
    @Operation(summary = "네이버 회원가입/로그인", description = "ALL")
    @GetMapping("/callback")
    public ResponseEntity<JwtToken> callback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        log.info("-----------come--------");
        // NOTE : 인가코드 인증 실패
        if(code == null){
            throw new FailedSocialAuthException("네이버 인증에 실패했습니다.");
        }

        String accessToken = naverService.getAccessToken(code);

        // NOTE : AccessToken 인증 실패
        if(accessToken == null){
            throw new FailedSocialAuthException("네이버 인증에 실패했습니다.");
        }

        User user = naverService.getUserInfo(accessToken);

        // NOTE : 사용자 정보 가져오기 실패
        if(user == null){
            throw new FailedSocialAuthException("네이버 인증에 실패했습니다.");
        }else{

            // NOTE : 회원가입
            if(userRepository.findByIdAndExpiredAtIsNull(user.getId()) == null){
                userRepository.save(user); // DB에 사용자 저장
                return ResponseEntity.ok().build();
            }

            // NOTE : 로그인
            JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), user.getRole());

            refreshTokenRepository.save(RefreshToken.of(jwtToken.getRefreshToken(), user.getId()));

            return ResponseEntity.ok().body(jwtToken);
        }
    }

}
