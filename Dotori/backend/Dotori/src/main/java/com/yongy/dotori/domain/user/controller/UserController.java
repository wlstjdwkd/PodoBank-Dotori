package com.yongy.dotori.domain.user.controller;

import com.yongy.dotori.domain.user.dto.request.UserInfoReqDto;
import com.yongy.dotori.domain.user.dto.request.UserLoginReqDto;
import com.yongy.dotori.domain.user.dto.response.UserInfoResDto;
import com.yongy.dotori.domain.user.entity.Provider;
import com.yongy.dotori.domain.user.entity.Role;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.ExceptionEnum;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.user.service.UserService;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.redis.repository.RefreshTokenRepository;

import com.yongy.dotori.global.redis.entity.RefreshToken;
import com.yongy.dotori.global.security.provider.JwtTokenProvider;
import com.yongy.dotori.global.security.dto.JwtToken;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

//    @Operation(summary = "회원가입에서 이메일 인증번호를 전송한다.")

    @PostMapping("/email/check-id")
    public ResponseEntity<? extends BaseResponseBody> validIdCheck(@RequestParam(name="id") String id){
        User user = userRepository.findByIdAndExpiredAtIsNull(id);

        if(user == null){
            // 이메일 인증을 한다.
            userService.emailCert(id);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "이메일 인증 번호를 전송했습니다."));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4001, ExceptionEnum.ALREADY_EXIST_ID));
        }
    }

    @PostMapping("/email/check-code")
    public ResponseEntity<? extends BaseResponseBody> validEmailCodeCheck(@RequestParam(name="code") String code){

        String authId = userService.getEmailAuthId(code); // 인증번호 검증

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(authId == null) { // 인증번호의 시간이 만료됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4002, ExceptionEnum.EXPIRED_AUTHCODE));
        }else if(authId.equals(user.getId())){
            userService.deleteEmailAuthCode(code); // 인증번호 삭제
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "인증 완료"));
        }else{ // 인증번호가 틀림
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4003, ExceptionEnum.INVALID_AUTHCODE));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<? extends BaseResponseBody> signup(@RequestBody UserInfoReqDto userInfoReqDto){
        try{
            User user = User.builder()
                    .id(userInfoReqDto.getId())
                    .password(passwordEncoder.encode(userInfoReqDto.getPassword()))
                    .userName(userInfoReqDto.getUserName())
                    .birthDate(LocalDate.parse(userInfoReqDto.getBirthDate()))
                    .phoneNumber(userInfoReqDto.getPhoneNumber())
                    .authProvider(Provider.DOTORI)
                    .role(Role.ROLE_USER)
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
    public ResponseEntity<? extends BaseResponseBody> dotoriLogin(@RequestBody UserLoginReqDto userLoginReqDto) {

        User user = userRepository.findByIdAndExpiredAtIsNull(userLoginReqDto.getId());

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "아이디를 확인해주세요."));
        }

        if(user.getAuthProvider().equals(Provider.DOTORI)){
            if(!passwordEncoder.matches(userLoginReqDto.getPassword(), user.getPassword())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "비밀번호를 확인해주세요."));
            }
        }

        JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), Role.ROLE_USER);

        // refreshToken 저장
        refreshTokenRepository.save(RefreshToken.of(userLoginReqDto.getId(), jwtToken.getRefreshToken()));

        // accessToken 전달
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, jwtToken));
    }


    // NOTE : RefreshToken이 유효하면 accessToken, refreshToken을 재발급 받아서 반환
    @PostMapping("/new-token")
    public ResponseEntity<? extends BaseResponseBody> generateNewToken(String refreshToken){

        // refreshToken이 유효하면
        if(refreshTokenRepository.findById(refreshToken) != null){

            String id = jwtTokenProvider.getUserId(refreshToken);
            JwtToken jwtToken = jwtTokenProvider.createToken(id , Role.ROLE_USER);

            // refreshToken 저장하기
            refreshTokenRepository.save(RefreshToken.of(jwtToken.getRefreshToken(), id));

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, jwtToken));
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(404, "다시 로그인하세요"));
    }

    // NOTE : 사용자 데이터 가져오기
    @GetMapping()
    public ResponseEntity<? extends BaseResponseBody> getUserInfo(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, UserInfoResDto.builder()
                .userSeq(user.getUserSeq())
                .id(user.getId())
                .birthDate(user.getBirthDate())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .authProvider(user.getAuthProvider()).build()));
    }


    // NOTE : 생년월일 수정
    @PatchMapping("/birthDate")
    public ResponseEntity<? extends BaseResponseBody>updateBirthDate(@RequestParam String birthDate){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBirthDate(LocalDate.parse(birthDate));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(200, "success"));
    }


    // NOTE : 핸드폰번호 수정
    @PatchMapping("/phoneNumber")
    public ResponseEntity<? extends BaseResponseBody>updatePhoneNumber(@RequestParam String phoneNumber){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(200, "success"));
    }

    // NOTE : 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<? extends BaseResponseBody>updatePassword(@RequestParam String beforePassword, @RequestParam String afterPassword){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.getAuthProvider().equals(Provider.DOTORI))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, "네이버, 카카오 로그인은 비밀번호를 변경할 수 없습니다."));

        if(passwordEncoder.encode(beforePassword).equals(user.getPassword())){
            user.setPassword(passwordEncoder.encode(afterPassword));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "비밀번호를 변경하였습니다."));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, "현재 비밀번호가 일치하지 않습니다."));
    }

    // NOTE : 로그아웃
    @PatchMapping("/logout")
    public ResponseEntity<? extends BaseResponseBody>logout(@RequestParam String refreshToken){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenRepository.deleteById(refreshToken);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(200, "success")); // FE에서 accessToken 삭제함
    }


    // NOTE : 탈퇴하기
    @PatchMapping("/retire")
    public ResponseEntity<? extends BaseResponseBody>retire(String refreshToken){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setExpiredAt(LocalDateTime.now());
        userRepository.save(user);
        refreshTokenRepository.deleteById(refreshToken);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(200, "success"));
    }


    // TODO : 명세서 보기
}
