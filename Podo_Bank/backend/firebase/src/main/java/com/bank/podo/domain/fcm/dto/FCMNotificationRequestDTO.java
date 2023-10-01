package com.bank.podo.domain.fcm.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FCMNotificationRequestDTO {
    private Long targetUserId;
    private String title;
    private String body;

    @Builder
    public FCMNotificationRequestDTO(Long targetUserId, String title, String body) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
    }

}
