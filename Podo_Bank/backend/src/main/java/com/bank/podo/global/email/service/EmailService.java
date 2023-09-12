package com.bank.podo.global.email.service;

import com.bank.podo.global.email.entity.VerificationCode;
import com.bank.podo.global.email.enums.VerificationType;
import com.bank.podo.global.email.exception.ResendTimeNotExpiredException;
import com.bank.podo.global.email.message.EmailMessage;
import com.bank.podo.global.email.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final int VERIFICATION_CODE_LENGTH = 8;

    private final JavaMailSender javaMailSender;
    private final EmailMessage emailMessage;
    private final VerificationCodeRepository verificationCodeRepository;

    public boolean sendVerificationCode(String email, VerificationType type) {
        VerificationCode verificationCode = verificationCodeRepository.findById(email).orElse(null);

        if (verificationCode != null && isResendTimeNotExpired(verificationCode)) {
            throw new ResendTimeNotExpiredException();
        }

        String code = generateCode();
        String subject = "[포도은행] 인증 코드";
        String info = gernerateEmailMessage(type, code);

//        boolean messageSendSuccess = sendMessage(email, subject, info);
        boolean messageSendSuccess = true;
        if(messageSendSuccess) {
            verificationCodeRepository.save(VerificationCode.builder()
                    .email(email)
                    .code(code)
                    .sendAt(LocalDateTime.now())
                    .build());
        }

        return messageSendSuccess;
    }

    public boolean checkVerificationCode(String code, String email) {
        VerificationCode verificationCode = verificationCodeRepository.findById(email).orElse(null);

        if(verificationCode == null) {
            return false;
        }

        if(verificationCode.getCode().equals(code)) {
            verificationCodeRepository.deleteById(email);
            return true;
        } else {
            return false;
        }
    }

    private boolean sendMessage(String email, String subject, String info) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setSubject(subject);
            message.setText(info, "UTF-8", "html");
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email, "", "UTF-8"));
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String gernerateEmailMessage(VerificationType type, String code) {
        if(type.equals(VerificationType.REGISTER)) {
            return emailMessage.generateRegisterMessage(code);
        } else if(type.equals(VerificationType.RESET_PASSWORD)) {
            return emailMessage.generatePasswordResetMessage(code);
        } else {
            return null;
        }

    }

    private boolean isResendTimeNotExpired(VerificationCode verificationCode) {
        return verificationCode.getSendAt().plusMinutes(1).isAfter(LocalDateTime.now());
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
            int randomNumber = random.nextInt(10); // 0부터 9까지의 난수 생성
            code.append(randomNumber);
        }

        return code.toString();
    }
}
