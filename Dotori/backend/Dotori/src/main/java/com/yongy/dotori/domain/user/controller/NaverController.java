package com.yongy.dotori.domain.user.controller;


import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.ExceptionEnum;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.global.redis.entity.RefreshToken;
import com.yongy.dotori.global.redis.repository.RefreshTokenRepository;
import com.yongy.dotori.global.security.dto.JwtToken;
import com.yongy.dotori.global.security.provider.JwtTokenProvider;
import com.yongy.dotori.domain.user.service.NaverService;
import com.yongy.dotori.global.common.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            @ApiResponse(responseCode = "5001", description = "네이버 인증에 실패했습니다.")
    })
    @Operation(summary = "네이버 회원가입/로그인", description = "사용자의 정보가 없으면 회원가입, 있으면 로그인")
    @GetMapping("/callback")
    public ResponseEntity<? extends BaseResponseBody> callback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        log.info("-----------come--------");
        // NOTE : 인가코드 인증 실패
        if(code == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(5001, ExceptionEnum.NAVER_AUTH_FAIL));
        }

        String accessToken = naverService.getAccessToken(code);

        // NOTE : AccessToken 인증 실패
        if(accessToken == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(5001, ExceptionEnum.NAVER_AUTH_FAIL));
        }

        User user = naverService.getUserInfo(accessToken);

        // NOTE : 사용자 정보 가져오기 실패
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(5001, ExceptionEnum.NAVER_AUTH_FAIL));
        }else{

            // NOTE : 회원가입
            if(userRepository.findByIdAndExpiredAtIsNull(user.getId()) == null){
                userRepository.save(user); // DB에 사용자 저장
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "네이버 회원가입 성공"));
            }

            // NOTE : 로그인
            JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), user.getRole());

            refreshTokenRepository.save(RefreshToken.of(jwtToken.getRefreshToken(), user.getId()));

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(201, jwtToken));
        }
    }

}
