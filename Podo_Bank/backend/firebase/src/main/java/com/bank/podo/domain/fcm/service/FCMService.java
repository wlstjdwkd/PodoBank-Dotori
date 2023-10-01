package com.bank.podo.domain.fcm.service;

import com.bank.podo.domain.fcm.dto.AddFCMTokenDTO;
import com.bank.podo.domain.fcm.dto.DeleteFCMTokenDTO;
import com.bank.podo.domain.fcm.dto.FCMNotificationRequestDTO;
import com.bank.podo.domain.fcm.entity.FCMToken;
import com.bank.podo.domain.fcm.repository.FCMRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final FirebaseMessaging firebaseMessaging;
    private final FCMRepository fcmRepository;

    @Transactional
    public void addToken(AddFCMTokenDTO addFCMTokenDTO) {
        fcmRepository.save(FCMToken.builder()
                .email(addFCMTokenDTO.getEmail())
                .token(addFCMTokenDTO.getToken())
                .build());
    }

    @Transactional
    public void deleteToken(DeleteFCMTokenDTO deleteFCMTokenDTO) {
        fcmRepository.deleteByEmail(deleteFCMTokenDTO.getEmail());
    }

    @Transactional(readOnly = true)
    public String sendNotificationByToken(FCMNotificationRequestDTO fcmNotificationRequestDTO) {
        Optional<FCMToken> fcmToken = fcmRepository.findById(fcmNotificationRequestDTO.getTargetUserId());

        if(fcmToken.isPresent()) {
            if(fcmToken.get().getToken() != null) {
                Notification notification = Notification.builder()
                        .setTitle(fcmNotificationRequestDTO.getTitle())
                        .setBody(fcmNotificationRequestDTO.getBody())
                        .build();

                Message message = Message.builder()
                        .setToken(fcmToken.get().getToken())
                        .setNotification(notification)
                        .build();
                try {
                    firebaseMessaging.send(message);
                    return "success";
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    return "fail";
                }
            } else {
                return "no token";
            }
        } else {
            return "no user";
        }
    }
}
