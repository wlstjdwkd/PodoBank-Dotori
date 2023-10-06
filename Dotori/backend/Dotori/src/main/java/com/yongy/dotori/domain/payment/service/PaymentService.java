package com.yongy.dotori.domain.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.chatGPT.dto.UnclassifiedResponseDTO;
import com.yongy.dotori.domain.payment.dto.PaymentDetailDTO;
import com.yongy.dotori.domain.payment.dto.UpdateUnclassifiedDTO;
import com.yongy.dotori.domain.payment.dto.response.PaymentPodoResDto;
import com.yongy.dotori.domain.payment.entity.Payment;
import org.json.simple.parser.ParseException;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    List<PaymentPodoResDto> getPayments(LocalDateTime updateTime, Long accountSeq) throws ParseException, JsonProcessingException;
    List<PaymentDetailDTO> findAllUnclassified(Long planSeq);
    void updateUnclassified(Long planSeq, UpdateUnclassifiedDTO updateUnclassifiedDTO);
}
