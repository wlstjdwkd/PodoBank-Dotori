package com.bank.podo.domain.user.controller;

import com.bank.podo.domain.user.dto.*;
import com.bank.podo.domain.user.service.UserService;
import com.bank.podo.global.email.dto.EmailVerificationCheckDTO;
import com.bank.podo.global.email.dto.EmailVerificationDTO;
import com.bank.podo.global.email.service.EmailService;
import com.bank.podo.global.security.entity.Token;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Api(tags = "유저관리")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "회원가입 실패"),
            @ApiResponse(responseCode = "409", description = "이미 사용중인 아이디"),
            @ApiResponse(responseCode = "422", description = "이메일 형식 오류")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 인증 코드 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 전송 성공"),
            @ApiResponse(responseCode = "400", description = "이메일 전송 실패"),
            @ApiResponse(responseCode = "422", description = "이메일 형식 오류"),
            @ApiResponse(responseCode = "429", description = "재전송 대기시간")
    })
    @PostMapping("/emailVerification")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody EmailVerificationDTO emailVerificationDTO) {
        userService.checkEmailFormat(emailVerificationDTO.getEmail());

        emailService.sendVerificationCode(emailVerificationDTO.getEmail(), emailVerificationDTO.getType());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 인증 코드 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드 일치"),
            @ApiResponse(responseCode = "403", description = "인증 코드 불일치")
    })
    @PostMapping("/emailVerification/check")
    public ResponseEntity<String> checkVerificationCode(@RequestBody EmailVerificationCheckDTO emailVerificationCheckDTO) {
        boolean isValidate = emailService.checkVerificationCode(emailVerificationCheckDTO.getEmail(), emailVerificationCheckDTO.getCode());

        if(isValidate) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).body("인증 코드가 일치하지 않습니다.");
        }
    }

    @Operation(summary = "아이디 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 사용 가능"),
            @ApiResponse(responseCode = "400", description = "이메일 사용 불가능"),
            @ApiResponse(responseCode = "422", description = "이메일 형식 오류")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Void> checkUsername(@PathVariable String email) {
        userService.checkUsername(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return userService.login(request, loginDTO, passwordEncoder);
    }

    @Operation(summary = "토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "토큰 재발급 실패")
    })
    @PostMapping("/refresh")
    public ResponseEntity<Token> refresh(@RequestParam String refreshToken, HttpServletRequest request) {
        return userService.refresh(refreshToken, request);
    }

    @Operation(summary = "로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패"),
            @ApiResponse(responseCode = "401", description = "jwt 인증 실패"),
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "회원 정보 조회 실패"),
            @ApiResponse(responseCode = "401", description = "jwt 인증 실패"),
    })
    @GetMapping("")
    public ResponseEntity<UserInfoDTO> getUserInfo() {
        UserInfoDTO userInfoDTO = userService.getUserInfo();
        return ResponseEntity.ok(userInfoDTO);
    }

    @Operation(summary = "비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호 변경 실패"),
            @ApiResponse(responseCode = "401", description = "jwt 인증 실패"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원"),
            @ApiResponse(responseCode = "422", description = "비밀번호 형식 오류")
    })
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "회원 탈퇴 실패"),
            @ApiResponse(responseCode = "401", description = "jwt 인증 실패"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원")
    })
    @PostMapping("")
    public ResponseEntity<Void> deleteUser(@RequestBody UserDeleteDTO userDeleteDTO) {
        userService.deleteUser(userDeleteDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }
}
