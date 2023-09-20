package com.yongy.dotori.domain.chatGPT.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Choice {
    int index;
    Message message;
    @JsonProperty("finish_reason")
    String finishReason;
}
