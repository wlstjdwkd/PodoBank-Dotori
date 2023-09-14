package com.yongy.dotori.domain.user.controller;

import com.yongy.dotori.domain.user.dto.request.UserInfoDto;
import com.yongy.dotori.domain.user.entity.Provider;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.ExceptionEnum;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.user.service.KakaoService;
import com.yongy.dotori.domain.user.service.NaverService;
import com.yongy.dotori.domain.user.service.UserService;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.redis.RedisUtil;
import com.yongy.dotori.global.security.jwt.JwtTokenProvider;
import com.yongy.dotori.global.security.jwtDto.JwtToken;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private NaverService naverService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider provider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtil redisUtil;

    private final long exp = 1000L * 60 * 60;

    @PostMapping("/check-id")
    public ResponseEntity<? extends BaseResponseBody> validIdCheck(@RequestParam(name="id") String id){
        User user = userRepository.findById(id);
        if(user == null){
            // 이메일 인증을 한다.
            userService.authEmail(id);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "이메일 인증을 끝냈습니다."));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4001, ExceptionEnum.ALREADY_EXIST_ID));
        }
    }

    @PostMapping("/check-code")
    public ResponseEntity<? extends BaseResponseBody> validCodeCheck(
            @RequestParam(name="id") String id,
            @RequestParam(name="code") String code){
        String authCode = userService.getAuthCode(id); // 인증번호 검증
        if(authCode == null) { // 인증번호의 시간이 만료됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4002, ExceptionEnum.EXPIRED_AUTHCODE));
        }else if(authCode.equals(code)){
            userService.deleteAuthCode(id); // 인증번호 삭제
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "유효한 코드입니다."));
        }else{ // 인증번호가 틀림
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4003, ExceptionEnum.INVALID_AUTHCODE));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<? extends BaseResponseBody> signup(@RequestBody UserInfoDto userInfoDto){
        try{
            User user = User.builder()
                    .role(userInfoDto.getRole()) // 사용자 또는 관리자
                    .id(userInfoDto.getId())
                    .password(passwordEncoder.encode(userInfoDto.getPassword())) // 사용자의 비밀번호를 암호화하기
                    .userName(userInfoDto.getUserName())
                    .birthDate(LocalDate.parse(userInfoDto.getBirthDate()))
                    .phoneNumber(userInfoDto.getPhoneNumber())
                    .authProvider(Provider.DOTORI)
                    .build();

            // 비밀번호 암호화해서 저장하기
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "회원가입을 완료했습니다."));
        }catch(Exception e){
            log.info("회원가입 오류");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4004, "회원가입 오류"));
        }

    }
//    @PostMapping("/dotori/signin")
//    public ResponseEntity<?> dotoriLogin(@RequestBody Map<String, String> loginForm) {
//
//    }

//    @PostMapping("/kakao/signin")
//    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> loginForm, @RequestHeader("Authorization") String accessToken) throws Exception {
//
//        // 토큰의 유효성을 검사한다.
//        if(kakaoService.validateToken(accessToken).getStatusCode() == HttpStatus.OK){
//            // 사용자의 정보가 DB에 있는지 확인한다.
//            String id = loginForm.get("id");
//            if(userRepository.findById(id) != null){
//                // 접근O
//            }else{
//                // 접근X
//            }
//        }else{
//            // 접근X
//        }
//    }

//    @PostMapping("/naver/signin")
//    public ResponseEntity<?> signin(@RequestBody Map<String, String> loginForm) {
//
//    }




}
