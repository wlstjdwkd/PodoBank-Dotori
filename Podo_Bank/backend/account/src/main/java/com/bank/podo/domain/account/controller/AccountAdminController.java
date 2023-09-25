package com.bank.podo.domain.account.controller;

import com.bank.podo.domain.account.dto.TransferDTO;
import com.bank.podo.domain.account.service.AccountAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/admin")
public class AccountAdminController {
    private final AccountAdminService accountAdminService;

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferDTO transferDTO) {
        accountAdminService.transfer(transferDTO);
    }

}
