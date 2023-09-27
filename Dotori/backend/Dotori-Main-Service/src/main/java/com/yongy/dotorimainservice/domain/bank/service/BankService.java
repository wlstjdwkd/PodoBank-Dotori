package com.yongy.dotorimainservice.domain.bank.service;

import com.yongy.dotori.domain.bank.dto.response.BankListDto;

import java.util.List;

public interface BankService {
    public List<BankListDto> BankList();
}
