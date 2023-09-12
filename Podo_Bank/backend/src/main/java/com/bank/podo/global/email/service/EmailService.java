package com.bank.podo.global.email.service;

import com.bank.podo.global.email.dto.EmailVerificationSuccessDTO;
import com.bank.podo.global.email.entity.VerificationCode;
import com.bank.podo.global.email.entity.VerificationSuccess;
import com.bank.podo.global.email.enums.VerificationType;
import com.bank.podo.global.email.exception.EmailVerificationException;
import com.bank.podo.global.email.exception.ResendTimeNotExpiredException;
import com.bank.podo.global.email.message.EmailMessage;
import com.bank.podo.global.email.repository.VerificationCodeRepository;
import com.bank.podo.global.email.repository.VerificationSuccessRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final int VERIFICATION_CODE_LENGTH = 8;

    private final JavaMailSender javaMailSender;
    private final EmailMessage emailMessage;

    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationSuccessRepository verificationSuccessRepository;

    public boolean sendVerificationCode(String email, VerificationType type) {
        VerificationCode verificationCode = verificationCodeRepository.findById(email).orElse(null);

        if (verificationCode != null && isResendTimeNotExpired(verificationCode)) {
            throw new ResendTimeNotExpiredException();
        }

        String code = generateCode();
        String subject = "[포도은행] 인증 코드";
        String info = gernerateEmailMessage(type, code);

        boolean messageSendSuccess = sendMessage(email, subject, info);

        if(messageSendSuccess) {
            verificationCodeRepository.save(VerificationCode.builder()
                    .email(email)
                    .code(code)
                    .sendAt(LocalDateTime.now())
                    .build());
        }

        return messageSendSuccess;
    }

    public EmailVerificationSuccessDTO checkVerificationCode(String email, String code) {
        VerificationCode verificationCode = verificationCodeRepository.findById(email).orElse(null);

        if(verificationCode == null) {
            throw new EmailVerificationException("인증 코드를 재발송 해주세요.");
        }

        if(!verificationCode.getCode().equals(code)) {
            throw new EmailVerificationException("인증 코드가 일치하지 않습니다.");
        }

        String successCode = UUID.randomUUID().toString();

        // 인증 성공 여부 저장
        verificationSuccessRepository.save(VerificationSuccess.builder()
                        .email(email)
                        .successAt(LocalDateTime.now())
                        .successCode(successCode)
                        .build());
        // 인증 코드 삭제
        verificationCodeRepository.deleteById(email);
        return EmailVerificationSuccessDTO.builder().email(email).successCode(successCode).build();
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
        // TODO: 수정 필요
        return verificationCode.getSendAt().plusSeconds(10).isAfter(LocalDateTime.now());
//        return verificationCode.getSendAt().plusMinutes(1).isAfter(LocalDateTime.now());
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
