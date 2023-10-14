package com.bank.podo.global.others.service;

import com.bank.podo.global.others.dto.FCMNotificationRequestDTO;
import com.bank.podo.global.request.RequestApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {

    @Value("${http.request.firebase.url}")
    private String firebaseUrl;

    private final RequestApi requestApi;

    private final ObjectMapper objectMapper;

    @Async
    public void sendNotification(String targetUserEmail, String title, String body) {
        String url = firebaseUrl + "/api/v1/fcm/send";
        FCMNotificationRequestDTO fcmNotificationRequestDTO = FCMNotificationRequestDTO.builder()
                .targetUserEmail(targetUserEmail)
                .title(title)
                .body(body)
                .build();

        try {
            requestApi.apiPost(url, objectMapper.writeValueAsString(fcmNotificationRequestDTO));
        } catch (JsonProcessingException e) {
            logSendNotification(targetUserEmail, title, body, false);
        }

        logSendNotification(targetUserEmail, title, body, true);
    }

    private void logSendNotification(String targetUserEmail, String title, String body, boolean success) {
        log.info("=====" + "\t"
                + "알림 전송" + "\t"
                + "유저: " + targetUserEmail + "\t"
                + "제목: " + title + "\t"
                + "내용: " + body + "\t"
                + "성공 여부: " + success + "\t"
                + "=====");
    }

}
