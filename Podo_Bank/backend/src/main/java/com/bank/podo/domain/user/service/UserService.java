package com.bank.podo.domain.user.service;

import com.bank.podo.domain.user.dto.ChangePasswordDTO;
import com.bank.podo.domain.user.dto.LoginDTO;
import com.bank.podo.domain.user.dto.RegisterDTO;
import com.bank.podo.domain.user.dto.UserInfoDTO;
import com.bank.podo.domain.user.enums.Role;
import com.bank.podo.domain.user.exception.AlreadyUsedUsernameException;
import com.bank.podo.domain.user.exception.PasswordFromatException;
import com.bank.podo.domain.user.exception.PasswordNotMatchException;
import com.bank.podo.domain.user.exception.UserNotFoundException;
import com.bank.podo.domain.user.repository.UserRepository;
import com.bank.podo.domain.user.entity.User;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtProvider jwtProvider;

    private final RequestHelper requestHelper;

    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public void register(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
        checkUsername(registerDTO.getId());

        checkPasswordFormat(registerDTO.getPassword());

        User user = toUserEntity(registerDTO, passwordEncoder);
        userRepository.save(user);
    }

    public void checkUsername(String id) {
        if(userRepository.existsById(id)) {
            throw new AlreadyUsedUsernameException("이미 사용중인 아이디입니다.");
        }
    }

    public ResponseEntity<?> login(HttpServletRequest request, LoginDTO loginDTO, PasswordEncoder passwordEncoder) {
        User user = userRepository.findById(loginDTO.getId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디입니다."));

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Token token = jwtProvider.generateToken(user.getId(), user.getRole());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));

        // refresh token 저장
        refreshTokenRedisRepository.save(RefreshToken.builder()
                        .id(user.getId())
                        .ip(requestHelper.getClientIp(request))
                        .authorities(authentication.getAuthorities())
                        .refreshToken(token.getRefreshToken())
                        .build());

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<Token> refresh(String token, HttpServletRequest request) {
        if(token != null && jwtProvider.verifyToken(token)) {

            RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);
            if(refreshToken != null) {
                Token newToken = jwtProvider.generateToken(refreshToken.getId(), Role.valueOf(refreshToken.getAuthorities().stream().findFirst().get().getAuthority()));

                refreshTokenRedisRepository.save(RefreshToken.builder()
                        .id(refreshToken.getId())
                        .ip(requestHelper.getClientIp(request))
                        .authorities(refreshToken.getAuthorities())
                        .refreshToken(newToken.getRefreshToken())
                        .build());
                return ResponseEntity.ok(newToken);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    public void logout() {
    }

    public UserInfoDTO getUserInfo() {
        User user = getLoginUser();
        return toUserInfoDTO(user);
    }

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
    }

    public void deleteUser() {
    }

    public User findUserByUserId(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디입니다."));
    }

    private User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkPasswordFormat(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        if(Pattern.compile(passwordPattern).matcher(password).matches()) {
            throw new PasswordFromatException("비밀번호는 영문, 숫자, 특수문자를 포함하여 8자리 이상이어야 합니다.");
        }
    }

    private User toUserEntity(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(registerDTO.getId())
                .password(registerDTO.getPassword())
                .name(registerDTO.getName())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .address(registerDTO.getAddress())
                .gender(registerDTO.getGender())
                .birthdate(registerDTO.getBirthdate())
                .contactInfo(registerDTO.getContactInfo())
                .build();
    }

    private UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .name(user.getName())
                .birthdate(user.getBirthdate())
                .gender(user.getGender())
                .contactInfo(user.getContactInfo())
                .address(user.getAddress())
                .id(user.getId())
                .build();
    }
}
