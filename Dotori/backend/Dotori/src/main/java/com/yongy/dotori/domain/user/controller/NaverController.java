package com.yongy.dotori.domain.user.controller;


import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.global.redis.entity.RefreshToken;
import com.yongy.dotori.global.redis.repository.RefreshTokenRepository;
import com.yongy.dotori.global.security.dto.JwtToken;
import com.yongy.dotori.global.security.provider.JwtTokenProvider;
import com.yongy.dotori.domain.user.service.NaverService;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/callback")
    public ResponseEntity<? extends BaseResponseBody> callback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");

        // NOTE : 인가코드 인증 실패
        if(code == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "인증에 실패했습니다."));
        }

        String accessToken = naverService.getAccessToken(code);

        // NOTE : AccessToken 인증 실패
        if(accessToken == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "인증에 실패했습니다."));
        }

        User user = naverService.getUserInfo(accessToken);

        // NOTE : 사용자 정보 가져오기 실패
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "인증에 실패했습니다."));
        }else{

            // NOTE : 회원가입
            if(userRepository.findById(user.getId()) == null){
                userRepository.save(user); // DB에 사용자 저장
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "회원가입에 성공하였습니다."));
            }

            // NOTE : 로그인
            JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), user.getRole());

            refreshTokenRepository.save(RefreshToken.of(jwtToken.getRefreshToken(), user.getId()));

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, jwtToken));
        }
    }

}
