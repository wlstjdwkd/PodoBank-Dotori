package com.bank.podo.domain.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class FCMConfig {

    @Value("${firebase.type}")
    String type;

    @Value("${firebase.project_id}")
    String project_id;

    @Value("${firebase.private_key_id}")
    String private_key_id;

    @Value("${firebase.private_key}")
    String private_key;

    @Value("${firebase.client_email}")
    String client_email;

    @Value("${firebase.client_id}")
    String client_id;

    @Value("${firebase.auth_uri}")
    String auth_uri;

    @Value("${firebase.token_uri}")
    String token_uri;

    @Value("${firebase.auth_provider_x509_cert_url}")
    String auth_provider_x509_cert_url;

    @Value("${firebase.client_x509_cert_url}")
    String client_x509_cert_url;

    @Value("${firebase.universe_domain}")
    String universe_domain;

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        InputStream refreshToken = getFirebaseRefreshToken();

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

        if(firebaseApps != null && !firebaseApps.isEmpty()) {
            for(FirebaseApp app : firebaseApps) {
                if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    firebaseApp = app;
                }
            }
        } else {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
        }

        return FirebaseMessaging.getInstance();
    }

    private InputStream getFirebaseRefreshToken() throws IOException {
        Map<String, String> map = new HashMap<>();

        map.put("type", type);
        map.put("project_id", project_id);
        map.put("private_key_id", private_key_id);
        map.put("private_key", private_key);
        map.put("client_email", client_email);
        map.put("client_id", client_id);
        map.put("auth_uri", auth_uri);
        map.put("token_uri", token_uri);
        map.put("auth_provider_x509_cert_url", auth_provider_x509_cert_url);
        map.put("client_x509_cert_url", client_x509_cert_url);
        map.put("universe_domain", universe_domain);

        JSONObject jsonObject = new JSONObject(map);
        String jsonString = jsonObject.toString();
        return new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
    }
}
