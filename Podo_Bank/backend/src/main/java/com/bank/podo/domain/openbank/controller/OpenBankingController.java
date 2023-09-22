package com.bank.podo.domain.openbank.controller;

import com.bank.podo.domain.account.dto.TransactionHistoryDTO;
import com.bank.podo.domain.openbank.dto.*;
import com.bank.podo.domain.openbank.service.OpenBankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fintech")
public class OpenBankingController {

    private final OpenBankingService openBankingService;

    @Operation(summary = "1원 송금", description = "MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1원 송금 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "무언가 존재하지 않습니다(서비스코드, 계좌 등)."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/oneCentVerification")
    public void oneCentVerification(@RequestBody FintechOneCentVerificationDTO fintechOneCentVerificationDTO) {
        openBankingService.oneCentVerification(fintechOneCentVerificationDTO);
    }

    @Operation(summary = "1원 송금 확인", description = "MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1원 송금 확인 성공"),
            @ApiResponse(responseCode = "400", description = "1원 송금 확인 실패(잘못된 요청, 1원 송금 확인 실패, 1원 송금 확인 만료 등)"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "무언가 존재하지 않습니다(서비스코드, 계좌 등)."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/oneCentVerification/check")
    public ResponseEntity<UserAccountFintechCodeDTO> oneCentVerificationCheck(@RequestBody FintechOneCentVerificationCheckDTO fintechOneCentVerificationDTO) {
        UserAccountFintechCodeDTO userAccountFintechCodeDTO = openBankingService.oneCentVerificationCheck(fintechOneCentVerificationDTO);

        return ResponseEntity.ok(userAccountFintechCodeDTO);
    }

    @Operation(summary = "사용자 계좌 출금", description = "MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 계좌 출금 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "무언가 존재하지 않습니다(서비스코드, 계좌 등)."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawUserAccount(@RequestBody FintechWithdrawDTO fintechWithdrawDTO) {
        openBankingService.withdrawUserAccount(fintechWithdrawDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 계좌 입금", description = "MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 계좌 입금 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "무언가 존재하지 않습니다(서비스코드, 계좌 등)."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/deposit")
    public ResponseEntity<Void> depositUserAccount(@RequestBody FintechDepositDTO fintechDepositDTO) {
        openBankingService.depositUserAccount(fintechDepositDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 계좌 잔액 조회", description = "MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 계좌 잔액 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "무언가 존재하지 않습니다(서비스코드, 계좌 등)."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/balance")
    public ResponseEntity<FintechUserBalanceDTO> getUserAccountBalance(@RequestBody FintechUserDTO fintechUserDTO) {
        FintechUserBalanceDTO fintechUserBalanceDTO = openBankingService.getUserAccountBalance(fintechUserDTO);
        return ResponseEntity.ok(fintechUserBalanceDTO);
    }

    @Operation(summary = "사용자 계좌 내역 조회", description = "MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 계좌 내역 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "무언가 존재하지 않습니다(서비스코드, 계좌 등)."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/history")
    public ResponseEntity<List<TransactionHistoryDTO>> getUserAccountTransactionHistory(@RequestBody FintechUserHistoryDTO fintechUserHistoryDTO) {
        List<TransactionHistoryDTO> transactionHistoryDTOList = openBankingService.getUserAccountTransactionHistory(fintechUserHistoryDTO);
        return ResponseEntity.ok(transactionHistoryDTOList);
    }
}
