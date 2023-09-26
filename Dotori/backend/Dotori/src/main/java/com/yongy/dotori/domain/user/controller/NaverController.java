package com.yongy.dotori.domain.user.controller;


import com.yongy.dotori.domain.user.entity.Provider;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.AlreadyExistIdException;
import com.yongy.dotori.domain.user.exception.FailedSocialAuthException;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.user.service.UserService;
import com.yongy.dotori.global.redis.entity.UserRefreshToken;
import com.yongy.dotori.global.redis.repository.UserRefreshTokenRepository;
import com.yongy.dotori.global.security.dto.JwtToken;
import com.yongy.dotori.global.security.provider.JwtTokenProvider;
import com.yongy.dotori.domain.user.service.NaverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/naver")
public class NaverController {

    @Autowired
    private NaverService naverService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
            User userFromDB = userService.getUser(user.getId());
            if(userFromDB == null){
                userService.saveUser(user);  // DB에 사용자 저장
                return ResponseEntity.ok().build();
            }

            // NOTE : 네이버 로그인
            if(userFromDB.getAuthProvider() == Provider.NAVER){
                JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), user.getRole());

                userService.saveUserRefreshToken(UserRefreshToken.of(user.getId(), jwtToken.getRefreshToken()));

                return ResponseEntity.ok().body(jwtToken);
            }

            // NOTE : 도토리 or 카카오로 회원가입이 되어있음
            throw new AlreadyExistIdException("다른 경로로 회원가입이 되어있습니다.");
        }
    }

}
