//package com.yongy.dotori.domain.user;
//
//import jakarta.annotation.PostConstruct;
//import net.nurigo.sdk.NurigoApp;
//import net.nurigo.sdk.message.model.Message;
//import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
//import net.nurigo.sdk.message.response.SingleMessageSentResponse;
//import net.nurigo.sdk.message.service.DefaultMessageService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
////import net.nurigo.java_sdk.exceptions;
//
//@Component
//public class SmsUtil {
//    @Value("${coolsms.api.key}")
//    private String apiKey;
//
//    @Value("${coolsms.api.secret}")
//    private String apiSecret;
//
//    private DefaultMessageService messageService;
//
//    @PostConstruct
//    private void init(){
//        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https:/");
//    }
//
//    public SingleMessageSentResponse sendMsgAuthCode(String phoneNumber, String authCode){
//        Message message = new Message();
//        message.setFrom("발신번호 입력");
//        message.setTo(phoneNumber);
//        message.setText("[Dotori] 아래의 인증번호를 입력해주세요\n" + authCode);
//
//        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
//    }
//
//}
