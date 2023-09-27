package com.yongy.dotoripurposeservice.domain.bank.service;


import com.yongy.dotoripurposeservice.domain.bank.dto.response.BankListDto;

import java.util.List;

public interface BankService {
    public List<BankListDto> BankList();
}
