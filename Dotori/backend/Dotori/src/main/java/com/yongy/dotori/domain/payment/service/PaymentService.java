package com.yongy.dotori.domain.payment.service;

import com.yongy.dotori.domain.payment.dto.response.PaymentPodoResDto;
import org.json.simple.parser.ParseException;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    List<PaymentPodoResDto> getPayments(LocalDateTime updateTime, Long accountSeq) throws ParseException;
}
