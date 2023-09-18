package com.yongy.dotori.domain.user.service;

import com.yongy.dotori.domain.user.repository.UserRepository;

import com.yongy.dotori.global.redis.entity.EmailAuth;
import com.yongy.dotori.global.redis.repository.EmailAuthRepository;
import com.yongy.dotori.global.redis.service.EmailAuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final JavaMailSender javaMailSender;
    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private EmailAuthService emailAuthService;

    public void authEmail(String id){
        // 임의의 authKey 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(8888) + 1111); // 1111 ~ 9999의 랜덤한 숫자

        // 이메일 발송
        sendAuthEmail(id, authKey);
    }

    public void sendAuthEmail(String id, String authKey){
        String subject = "도토리의 인증번호";
        String text = "회원 가입을 위한 인증번호는 " + authKey + "입니다. <br/>";

        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(id);
            helper.setSubject(subject);
            helper.setText(text, true); // 포함된 텍스트가 HTML이라는 의미로 true이다.
            javaMailSender.send(mimeMessage);

        }catch(MessagingException e){
            e.printStackTrace();
        }

        // 유효 시간(5분)동안 {email, authKey}를 저장한다.
        emailAuthRepository.save(EmailAuth.of(authKey, id));
    }

    public String getAuthCode(String id){
        return emailAuthService.getEmailAuth(id).getAuthCode();
    }

    public void deleteAuthCode(String id){
        emailAuthRepository.deleteById(id);
    }
}
