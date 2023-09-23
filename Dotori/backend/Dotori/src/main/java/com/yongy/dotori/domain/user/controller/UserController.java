package com.yongy.dotori.domain.user.controller;

import com.yongy.dotori.domain.user.dto.request.UserEmailReqDto;
import com.yongy.dotori.domain.user.dto.request.UserInfoReqDto;
import com.yongy.dotori.domain.user.dto.request.UserLoginReqDto;
import com.yongy.dotori.domain.user.dto.response.UserInfoResDto;
import com.yongy.dotori.domain.user.entity.Provider;
import com.yongy.dotori.domain.user.entity.Role;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.*;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.user.service.UserService;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.redis.repository.RefreshTokenRepository;

import com.yongy.dotori.global.redis.entity.RefreshToken;
import com.yongy.dotori.global.security.provider.JwtTokenProvider;
import com.yongy.dotori.global.security.dto.JwtToken;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name= "users", description = "사용자 API")
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


    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "이메일 인증코드 전송"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자입니다.")
    })
    @Operation(summary= "회원가입의 이메일 인증", description = "ALL")
    @PostMapping("/email/check-id")
    public ResponseEntity<Void> validIdCheck(@RequestParam(name="id") String id){
        User user = userRepository.findByIdAndExpiredAtIsNull(id);
        if(user == null){
            userService.emailCert(id);
            return ResponseEntity.ok().build();
        }
        throw new AlreadyExistIdException("이미 존재하는 사용자입니다.");
    }

    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "이메일 인증 완료"),
            @ApiResponse(responseCode = "404", description = "인증번호가 만료되었습니다."),
            @ApiResponse(responseCode = "409", description = "인증번호가 올바르지 않습니다."),
    })
    @Operation(summary = "회원가입의 이메일 인증코드 확인", description = "ALL")
    @PostMapping("/email/check-code")
    public ResponseEntity<Void> validEmailCodeCheck(@RequestBody UserEmailReqDto userEmailReqDto){

        // NOTE : RedisDB에서 인증코드가 존재하면 사용자의 아이디를 가져온다.
        String authId = userService.getEmailAuthId(userEmailReqDto.getCode());

        if(authId == null) { // 인증번호의 시간이 만료됨
            throw new ExpiredAuthCodeException("인증번호가 만료되었습니다.");
        }else if(authId.equals(userEmailReqDto.getId())){ // 인증번호 일치
            userService.deleteEmailAuthCode(userEmailReqDto.getCode()); // 인증번호 삭제
            return ResponseEntity.ok().build();
        }else{ // 인증번호 불일치
            throw new InvalidAuthCodeException("인증번호가 올바르지 않습니다.");
        }
    }

    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description = "회원가입을 완료했습니다."),
            @ApiResponse(responseCode = "400", description = "회원가입에 실패했습니다."),
    })
    @Operation(summary = "사용자 회원가입", description = "ALL")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserInfoReqDto userInfoReqDto){
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

            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.info("회원가입 오류");
            e.printStackTrace();
            throw new FailedSignupException("회원가입에 실패했습니다.");
        }
    }

    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "404", description = "아이디를 확인해주세요."),
            @ApiResponse(responseCode = "404", description = "비밀번호를 확인해주세요.")
    })
    @Operation(summary = "도토리 로그인", description = "ALL")
    @PostMapping("/signin")
    public ResponseEntity<JwtToken> dotoriLogin(@RequestBody UserLoginReqDto userLoginReqDto) {

        User user = userRepository.findByIdAndExpiredAtIsNull(userLoginReqDto.getId());

        // NOTE : 아이디 확인
        if(user == null){
            throw new InvalidIdException("아이디를 확인해주세요.");
        }

        // NOTE : 비밀번호 확인
        if(user.getAuthProvider().equals(Provider.DOTORI)){
            if(!passwordEncoder.matches(userLoginReqDto.getPassword(), user.getPassword())){
                throw new InvalidPwdException("비밀번호를 확인해주세요.");
            }
        }

        JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), Role.ROLE_USER);

        // refreshToken 저장
        refreshTokenRepository.save(RefreshToken.of(userLoginReqDto.getId(), jwtToken.getRefreshToken()));

        // accessToken, refreshToken 전달
        return ResponseEntity.ok().body(jwtToken);
    }


    // NOTE : RefreshToken이 유효하면 accessToken, refreshToken을 재발급
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "404", description = "다시 로그인 해주세요.")
    })
    @Operation(summary = "새로운 토큰 발급", description = "ALL")
    @PostMapping("/new-token")
    public ResponseEntity<JwtToken> generateNewToken(String refreshToken){

        // refreshToken이 유효한 경우
        if(refreshTokenRepository.findById(refreshToken) != null){

            String id = jwtTokenProvider.getUserId(refreshToken);
            JwtToken jwtToken = jwtTokenProvider.createToken(id , Role.ROLE_USER);

            // refreshToken 저장하기
            refreshTokenRepository.save(RefreshToken.of(jwtToken.getRefreshToken(), id));

            return ResponseEntity.ok().body(jwtToken);
        }

        throw new ExpiredAuthCodeException("다시 로그인 해주세요.");
    }

    // NOTE : 사용자 데이터 가져오기
    @ApiResponse(responseCode = "200", description = "사용자의 데이터를 가져오는데 성공함")
    @Operation(summary = "사용자 데이터 가져오기", description = "USER")
    @GetMapping()
    public ResponseEntity<UserInfoResDto> getUserInfo(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok().body(UserInfoResDto.builder()
                .userSeq(user.getUserSeq())
                .id(user.getId())
                .birthDate(user.getBirthDate())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .authProvider(user.getAuthProvider()).build());
    }


    // NOTE : 생년월일 수정
    @ApiResponse(responseCode = "200", description = "사용자의 생년월일 업데이트 완료")
    @Operation(summary = "사용자의 생년월일 업데이트", description = "USER")
    @PatchMapping("/birthDate")
    public ResponseEntity<Void>updateBirthDate(@RequestParam String birthDate){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBirthDate(LocalDate.parse(birthDate));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }


    // NOTE : 핸드폰번호 수정
    @ApiResponse(responseCode = "200", description = "사용자의 헨드폰번호 업데이트 완료")
    @Operation(summary = "사용자의 헨드폰번호 업데이트", description = "USER")
    @PatchMapping("/phoneNumber")
    public ResponseEntity<?>updatePhoneNumber(@RequestParam String phoneNumber){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // NOTE : 비밀번호 변경
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "사용자 비밀번호 변경 완료"),
            @ApiResponse(responseCode = "403", description = "네이버, 카카오 로그인은 비밀번호를 변경할 수 없습니다."),
            @ApiResponse(responseCode = "404", description = "비밀번호를 확인해주세요.")

    })
    @Operation(summary = "사용자의 비밀번호 업데이트", description = "USER")
    @PatchMapping("/password")
    public ResponseEntity<?>updatePassword(@RequestParam String beforePassword, @RequestParam String afterPassword){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.getAuthProvider().equals(Provider.DOTORI))
            throw new AccessDeniedSocialPwdException("네이버, 카카오 로그인은 비밀번호를 변경할 수 없습니다.");

        if(passwordEncoder.encode(beforePassword).equals(user.getPassword())){
            user.setPassword(passwordEncoder.encode(afterPassword));
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }

        throw new InvalidPwdException("비밀번호를 확인해주세요.");
    }

    // NOTE : 로그아웃
    @ApiResponse(responseCode = "200", description = "사용자 로그아웃 완료")
    @Operation(summary = "사용자 로그아웃", description = "USER")
    @PatchMapping("/logout")
    public ResponseEntity<String>logout(@RequestParam String refreshToken){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenRepository.deleteById(refreshToken);
        return ResponseEntity.ok().build(); // FE에서 accessToken 삭제함
    }


    // NOTE : 탈퇴하기
    @ApiResponse(responseCode = "200", description = "사용자 탈퇴 완료")
    @Operation(summary = "사용자 탈퇴하기", description = "USER")
    @PatchMapping("/retire")
    public ResponseEntity<String>retire(String refreshToken){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setExpiredAt(LocalDateTime.now());
        userRepository.save(user);
        refreshTokenRepository.deleteById(refreshToken);
        return ResponseEntity.ok().build(); // FE에서 accessToken 삭제함
    }


    // TODO : 명세서 보기
}
