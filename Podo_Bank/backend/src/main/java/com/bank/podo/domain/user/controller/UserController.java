package com.bank.podo.domain.user.controller;

import com.bank.podo.domain.user.dto.*;
import com.bank.podo.domain.user.service.UserService;
import com.bank.podo.global.security.entity.Token;
import com.bank.podo.global.security.service.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
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
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입")
    @PutMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "아이디 중복 체크")
    @GetMapping("/id/{id}")
    public ResponseEntity<Void> checkUsername(@PathVariable String id) {
        userService.checkUsername(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return userService.login(request, loginDTO, passwordEncoder);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<Token> refresh(@RequestParam String refreshToken, HttpServletRequest request) {
        return userService.refresh(refreshToken, request);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 조회")
    @GetMapping("")
    public ResponseEntity<UserInfoDTO> getUserInfo() {
        UserInfoDTO userInfoDTO = userService.getUserInfo();
        return ResponseEntity.ok(userInfoDTO);
    }

    @Operation(summary = "비밀번호 변경")
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser(@RequestBody UserDeleteDTO userDeleteDTO) {
        userService.deleteUser(userDeleteDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }
}
