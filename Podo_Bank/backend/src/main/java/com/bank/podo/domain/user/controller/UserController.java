package com.bank.podo.domain.user.controller;

import com.bank.podo.domain.user.dto.RegisterDTO;
import com.bank.podo.domain.user.dto.ChangePasswordDTO;
import com.bank.podo.domain.user.dto.LoginDTO;
import com.bank.podo.domain.user.dto.UserInfoDTO;
import com.bank.podo.domain.user.entity.User;
import com.bank.podo.domain.user.service.UserService;
import com.bank.podo.global.security.entity.Token;
import com.bank.podo.global.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/register")
//    @Operation(summary = "회원가입")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
//            @ApiResponse(responseCode = "400", description = "회원가입 실패")
//    })
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Void> checkUsername(@PathVariable String id) {
        userService.checkUsername(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO, passwordEncoder);
        Token token = tokenService.generateToken(user.getId(), user.getRole());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<UserInfoDTO> getUserInfo() {
        UserInfoDTO userInfoDTO = userService.getUserInfo();
        return ResponseEntity.ok(userInfoDTO);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok().build();
    }
}
