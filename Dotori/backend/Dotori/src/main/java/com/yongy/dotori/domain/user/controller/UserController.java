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
            @ApiResponse(responseCode = "4001", description = "이미 존재하는 사용자입니다.")
    })
    @Operation(summary= "회원가입의 이메일 인증", description = "사용자의 이메일로 인증번호를 전송한다.")
    @PostMapping("/email/check-id")
    public ResponseEntity<? extends BaseResponseBody> validIdCheck(@RequestParam(name="id") String id){
        User user = userRepository.findByIdAndExpiredAtIsNull(id);
        if(user == null){
            userService.emailCert(id);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "이메일 인증코드 전송"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4001, ExceptionEnum.ALREADY_EXIST_ID));
    }

    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "이메일 인증 완료"),
            @ApiResponse(responseCode = "4002", description = "인증번호가 만료되었습니다."),
            @ApiResponse(responseCode = "4003", description = "인증번호가 올바르지 않습니다."),
    })
    @Operation(summary = "회원가입의 이메일 인증코드 확인", description = "사용자 인증코드의 유효성 체크")
    @PostMapping("/email/check-code")
    public ResponseEntity<? extends BaseResponseBody> validEmailCodeCheck(@RequestParam(name="code") String code){

        // NOTE : RedisDB에서 인증코드가 존재하면 사용자의 아이디를 가져온다.
        String authId = userService.getEmailAuthId(code);

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(authId == null) { // 인증번호의 시간이 만료됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4002, ExceptionEnum.EXPIRED_AUTHCODE));
        }else if(authId.equals(user.getId())){ // 인증번호 일치
            userService.deleteEmailAuthCode(code); // 인증번호 삭제
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "이메일 인증 완료"));
        }else{ // 인증번호 불일치
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4003, ExceptionEnum.INVALID_AUTHCODE));
        }
    }

    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description = "회원가입을 완료했습니다."),
            @ApiResponse(responseCode = "4004", description = "회원가입에 실패했습니다."),
    })
    @Operation(summary = "사용자 회원가입")
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4004, ExceptionEnum.ERROR_DOTORI_SIGNUP));
        }
    }

    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "4005", description = "아이디를 확인해주세요."),
            @ApiResponse(responseCode = "4006", description = "비밀번호를 확인해주세요.")
    })
    @Operation(summary = "도토리 로그인")
    @PostMapping("/signin")
    public ResponseEntity<? extends BaseResponseBody> dotoriLogin(@RequestBody UserLoginReqDto userLoginReqDto) {

        User user = userRepository.findByIdAndExpiredAtIsNull(userLoginReqDto.getId());

        // NOTE : 아이디 확인
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4005, ExceptionEnum.INVALID_DOTORI_ID));
        }

        // NOTE : 비밀번호 확인
        if(user.getAuthProvider().equals(Provider.DOTORI)){
            if(!passwordEncoder.matches(userLoginReqDto.getPassword(), user.getPassword())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4006, ExceptionEnum.INVALID_DOTORI_PWD));
            }
        }

        JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), Role.ROLE_USER);

        // refreshToken 저장
        refreshTokenRepository.save(RefreshToken.of(userLoginReqDto.getId(), jwtToken.getRefreshToken()));

        // accessToken 전달
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, jwtToken));
    }


    // NOTE : RefreshToken이 유효하면 accessToken, refreshToken을 재발급
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "4007", description = "다시 로그인 해주세요.")
    })
    @Operation(summary = "새로운 토큰 발급", description = "새로운 토큰을 발급한다.")
    @PostMapping("/new-token")
    public ResponseEntity<? extends BaseResponseBody> generateNewToken(String refreshToken){

        // refreshToken이 유효한 경우
        if(refreshTokenRepository.findById(refreshToken) != null){

            String id = jwtTokenProvider.getUserId(refreshToken);
            JwtToken jwtToken = jwtTokenProvider.createToken(id , Role.ROLE_USER);

            // refreshToken 저장하기
            refreshTokenRepository.save(RefreshToken.of(jwtToken.getRefreshToken(), id));

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, jwtToken));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4007, ExceptionEnum.INVALID_REFRESH_TOKEN));
    }

    // NOTE : 사용자 데이터 가져오기
    @ApiResponse(responseCode = "200", description = "사용자의 데이터를 가져오는데 성공함")
    @Operation(summary = "사용자 데이터 가져오기")
    @GetMapping()
    public ResponseEntity<? extends BaseResponseBody> getUserInfo(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, UserInfoResDto.builder()
                .userSeq(user.getUserSeq())
                .id(user.getId())
                .birthDate(user.getBirthDate())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .authProvider(user.getAuthProvider()).build()));
    }


    // NOTE : 생년월일 수정
    @ApiResponse(responseCode = "200", description = "사용자의 생년월일 업데이트 완료")
    @Operation(summary = "사용자의 생년월일 업데이트")
    @PatchMapping("/birthDate")
    public ResponseEntity<? extends BaseResponseBody>updateBirthDate(@RequestParam String birthDate){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBirthDate(LocalDate.parse(birthDate));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "사용자의 생년월일 업데이트 완료"));
    }


    // NOTE : 핸드폰번호 수정
    @ApiResponse(responseCode = "200", description = "사용자의 헨드폰번호 업데이트 완료")
    @Operation(summary = "사용자의 헨드폰번호 업데이트")
    @PatchMapping("/phoneNumber")
    public ResponseEntity<? extends BaseResponseBody>updatePhoneNumber(@RequestParam String phoneNumber){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "사용자의 헨드폰번호 업데이트 완료"));
    }

    // NOTE : 비밀번호 변경
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "사용자 비밀번호 변경 완료"),
            @ApiResponse(responseCode = "4008", description = "네이버, 카카오 로그인은 비밀번호를 변경할 수 없습니다."),
            @ApiResponse(responseCode = "4006", description = "비밀번호를 확인해주세요.")

    })
    @Operation(summary = "사용자의 비밀번호 업데이트")
    @PatchMapping("/password")
    public ResponseEntity<? extends BaseResponseBody>updatePassword(@RequestParam String beforePassword, @RequestParam String afterPassword){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.getAuthProvider().equals(Provider.DOTORI))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4008, ExceptionEnum.NOT_UPDATE_SOCIAL_LOGIN));

        if(passwordEncoder.encode(beforePassword).equals(user.getPassword())){
            user.setPassword(passwordEncoder.encode(afterPassword));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "사용자 비밀번호 변경 완료"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4006, ExceptionEnum.INVALID_DOTORI_PWD));
    }

    // NOTE : 로그아웃
    @ApiResponse(responseCode = "200", description = "사용자 로그아웃 완료")
    @Operation(summary = "사용자 로그아웃")
    @PatchMapping("/logout")
    public ResponseEntity<? extends BaseResponseBody>logout(@RequestParam String refreshToken){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenRepository.deleteById(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "사용자 로그아웃 완료")); // FE에서 accessToken 삭제함
    }


    // NOTE : 탈퇴하기
    @ApiResponse(responseCode = "200", description = "사용자 탈퇴 완료")
    @Operation(summary = "사용자 탈퇴하기")
    @PatchMapping("/retire")
    public ResponseEntity<? extends BaseResponseBody>retire(String refreshToken){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setExpiredAt(LocalDateTime.now());
        userRepository.save(user);
        refreshTokenRepository.deleteById(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "사용자 탈퇴 완료")); // FE에서 accessToken 삭제함
    }


    // TODO : 명세서 보기
}
