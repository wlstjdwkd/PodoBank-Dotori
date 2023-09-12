package com.yongy.dotori.domain.user.controller;

import com.yongy.dotori.domain.user.dto.request.UserInfoDto;
import com.yongy.dotori.domain.user.entity.Provider;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.ExceptionEnum;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.user.service.UserService;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.security.jwt.JwtTokenProvider;
import com.yongy.dotori.global.security.jwtDto.JwtToken;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private UserService userService;

    @Autowired
    private JwtTokenProvider provider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        String authCode = userService.getAuthCode(code); // 인증번호 검증
        if(authCode == null) { // 인증번호의 시간이 만료됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4002, ExceptionEnum.EXPIRED_AUTHCODE));
        }else if(authCode.equals(id)){
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
                    .role(userInfoDto.getRole())
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


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Map<String, String> loginForm){
        String id = loginForm.get("id");

        User user = userRepository.findById(id);

        if(user == null){
            // 유효한 토큰이지만 DB에 데이터가 없는 경우
            return null;
        }else{
//            User authUser = provider.getAuthUser();
//
//            if(authUser != null && !authUser.getId().equals(id)){
//                JwtToken newToken = provider.createToken(user.getId(), user.getRole());
//                return new ResponseEntity<>(newToken, HttpStatus.OK);
//            }
            JwtToken newToken = provider.createToken(user.getId(), user.getRole());

            return new ResponseEntity<>(newToken, HttpStatus.OK);

           // return new ResponseEntity<>("이미 인증이 완료된 토큰입니다.", HttpStatus.OK);
        }


    }

}
