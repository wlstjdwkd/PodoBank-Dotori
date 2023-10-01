package com.yongy.dotorimainservice.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotorimainservice.domain.account.dto.AccountDTO;

import java.util.List;

public interface AccountService {
    List<AccountDTO> findAllAccount() throws JsonProcessingException;
    void removeUserAccounts(Long userSeq);
}
