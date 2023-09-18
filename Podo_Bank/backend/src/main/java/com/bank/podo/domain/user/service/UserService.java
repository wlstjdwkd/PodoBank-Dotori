package com.bank.podo.domain.user.service;

import com.bank.podo.domain.user.dto.*;
import com.bank.podo.domain.user.entity.User;
import com.bank.podo.domain.user.enums.Role;
import com.bank.podo.domain.user.exception.AlreadyUsedUsernameException;
import com.bank.podo.domain.user.exception.FromatException;
import com.bank.podo.domain.user.exception.PasswordNotMatchException;
import com.bank.podo.domain.user.repository.UserRepository;
import com.bank.podo.global.email.entity.VerificationSuccess;
import com.bank.podo.global.email.repository.VerificationSuccessRepository;
import com.bank.podo.global.others.service.RequestHelper;
import com.bank.podo.global.security.entity.RefreshToken;
import com.bank.podo.global.security.entity.Token;
import com.bank.podo.global.security.repository.RefreshTokenRedisRepository;
import com.bank.podo.global.security.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final VerificationSuccessRepository verificationSuccessRepository;

    @Transactional
    public void register(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
        VerificationSuccess verificationSuccess = verificationSuccessRepository.findById(registerDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 인증이 만료되었습니다."));

        if(!verificationSuccess.getSuccessCode().equals(registerDTO.getSuccessCode())) {
            throw new IllegalArgumentException("이메일 인증이 만료되었습니다.");
        }

        checkPasswordFormat(registerDTO.getPassword());

        checkUsername(registerDTO.getEmail());

        User user = toUserEntity(registerDTO, passwordEncoder);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void checkUsername(String email) {
        checkEmailFormat(email);
        if(userRepository.existsByEmail(email)) {
            throw new AlreadyUsedUsernameException("이미 사용중인 아이디입니다.");
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> login(HttpServletRequest request, LoginDTO loginDTO, PasswordEncoder passwordEncoder) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디, 비밀번호를 확인해주세요."));

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디, 비밀번호를 확인해주세요.");
        }

        Token token = jwtProvider.generateToken(user.getEmail(), user.getRole());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));

        // refresh token 저장
        refreshTokenRedisRepository.save(RefreshToken.builder()
                        .id(user.getEmail())
                        .ip(RequestHelper.getClientIp(request))
                        .authorities(authentication.getAuthorities())
                        .refreshToken(token.getRefreshToken())
                        .build());

        return ResponseEntity.ok(token);
    }

    @Transactional
    public ResponseEntity<Token> refresh(String token, HttpServletRequest request) {
        if(token != null && jwtProvider.verifyToken(token)) {

            RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);
            if(refreshToken != null) {
                Token newToken = jwtProvider.generateToken(refreshToken.getId(), Role.valueOf(refreshToken.getAuthorities().stream().findFirst().get().getAuthority()));

                refreshTokenRedisRepository.save(RefreshToken.builder()
                        .id(refreshToken.getId())
                        .ip(RequestHelper.getClientIp(request))
                        .authorities(refreshToken.getAuthorities())
                        .refreshToken(newToken.getRefreshToken())
                        .build());
                return ResponseEntity.ok(newToken);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Transactional
    public void logout() {
        User user = getLoginUser();
        refreshTokenRedisRepository.deleteById(user.getEmail());
    }

    @Transactional(readOnly = true)
    public UserInfoDTO getUserInfo() {
        User user = getLoginUser();
        return toUserInfoDTO(user);
    }

    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();

        if(!passwordEncoder.matches(changePasswordDTO.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("기존 비밀번호가 일치하지 않습니다.");
        }

        if(changePasswordDTO.getPassword().equals(changePasswordDTO.getNewPassword())) {
            throw new PasswordNotMatchException("기존 비밀번호와 동일합니다.");
        }

        checkPasswordFormat(changePasswordDTO.getNewPassword());

        userRepository.save(user.update(User.builder()
                .password(passwordEncoder.encode(changePasswordDTO.getNewPassword()))
                .build()));

        //TODO: 로그아웃 처리
    }

    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO, PasswordEncoder passwordEncoder) {
        VerificationSuccess verificationSuccess = verificationSuccessRepository.findById(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 인증이 만료되었습니다."));

        if(!verificationSuccess.getSuccessCode().equals(resetPasswordDTO.getSuccessCode())) {
            throw new IllegalArgumentException("이메일 인증이 만료되었습니다.");
        }

        User user = userRepository.findByEmail(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        checkPasswordFormat(resetPasswordDTO.getNewPassword());

        userRepository.save(user.update(User.builder()
                .password(passwordEncoder.encode(resetPasswordDTO.getNewPassword()))
                .build()));
    }

    @Transactional
    public void deleteUser(UserDeleteDTO userDeleteDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();

        if(!passwordEncoder.matches(userDeleteDTO.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.save(user.delete());
    }

    private User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void checkEmailFormat(String email) {
        String emailPattern =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if(!Pattern.compile(emailPattern).matcher(email).matches()) {
            throw new FromatException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void checkPasswordFormat(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@$!%*#?&])(?=\\S+$).{8,}$";

        if(!Pattern.compile(passwordPattern).matcher(password).matches()) {
            throw new FromatException("비밀번호는 영문, 숫자, 특수문자를 포함하여 8자리 이상이어야 합니다.");
        }
    }

    private User toUserEntity(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(registerDTO.getEmail())
                .password(registerDTO.getPassword())
                .name(registerDTO.getName())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .birthdate(registerDTO.getBirthdate())
                .phoneNumber(registerDTO.getPhoneNumber())
                .build();
    }

    private UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .name(user.getName())
                .birthdate(user.getBirthdate())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
