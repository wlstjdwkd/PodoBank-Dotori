package com.yongy.dotorimainservice.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotorimainservice.domain.account.dto.AccountDTO;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountNumberTitleReqDto;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountReqDto;
import com.yongy.dotorimainservice.domain.account.entity.Account;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<AccountDTO> findAllAccount() throws JsonProcessingException;
    void removeUserAllAccounts(Long userSeq) throws ParseException;
    Account getUserAccount(String accountNumber);

    void saveAccountTitle(AccountNumberTitleReqDto accountNumberTitleReqDto);

    void saveAccount(AccountReqDto accountReqDto);
    BigDecimal getBalance(Long accountSeq) throws JsonProcessingException;

    void removeUserAccount(Long accountSeq) throws ParseException;
}
