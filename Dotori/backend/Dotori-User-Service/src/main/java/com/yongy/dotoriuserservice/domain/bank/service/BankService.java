package com.yongy.dotoriuserservice.domain.bank.service;


import com.yongy.dotoriuserservice.domain.bank.dto.response.BankListDto;

import java.util.List;

public interface BankService {
    public List<BankListDto> BankList();
}
