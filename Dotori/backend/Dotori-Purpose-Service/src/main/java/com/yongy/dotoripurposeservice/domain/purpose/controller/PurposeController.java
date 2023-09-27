package com.yongy.dotoripurposeservice.domain.purpose.controller;

import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeAllDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeDetailDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeSummaryDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.communication.UserSeqDto;
import com.yongy.dotoripurposeservice.domain.purpose.service.PurposeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/purpose")
public class PurposeController {
    private final PurposeServiceImpl purposeService;

    @Operation(summary = "새로운 목표 생성")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "목표 생성 성공")
    })
    @PostMapping()
    public ResponseEntity<Void> createPurpose(@RequestBody PurposeDTO purposeDTO){
        // 새로운 목표 생성
        purposeService.createPurpose(purposeDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "전체 목표 리스트 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "목표 리스트 조회 성공")
    })
    @GetMapping()
    public ResponseEntity<PurposeAllDTO> findAllPurpose(){
        // 전체 목표 리스트 조회
        PurposeAllDTO result = purposeService.findAllPurpose();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "목표 상세 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "목표 상세 조회 성공")
    })
    @GetMapping("/{purposeSeq}")
    public ResponseEntity<PurposeDetailDTO> findPurposeDetail(@PathVariable("purposeSeq") Long purposeSeq){
        // 목표 조회
        // TODO 로그인 된 사용자의 purpose가 맞는지 확인하는 작업 필요
        PurposeDetailDTO purposeDetailDTO = purposeService.findPurposeDetail(purposeSeq);
        return ResponseEntity.ok(purposeDetailDTO);
    }


    @Operation(summary = "목표 중단")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "목표 중단 성공")
    })
    @PatchMapping("/terminate/{purposeSeq}")
    public ResponseEntity<Void> terminatePurpose(@PathVariable("purposeSeq") Long purposeSeq){
        // 목표 중단하기
        purposeService.terminatePurpose(purposeSeq);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "목표 진행 현황")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "목표 진행 현황 반환 성공")
    })
    @GetMapping("/terminate/{purposeSeq}")
    public ResponseEntity<PurposeSummaryDTO> terminateCheck(@PathVariable("purposeSeq") Long purposeSeq){
        // 목표 중단하기 눌렀을 때 현재 목표 진행 현황 보여줌
        PurposeSummaryDTO summary = purposeService.summarizePurpose(purposeSeq);
        return ResponseEntity.ok(summary);
    }


    // ---------------------------------통신---------------------------------
    // NOTE : 사용자의 목표 계좌에 남아있는 전체 금액을 반환한다.
    @PostMapping("/communication")
    public ResponseEntity<String> totalPurposeMoney(@RequestBody UserSeqDto userSeqDto){
        log.info("come");
        BigDecimal totalMoney = purposeService.totalPurposeMoney(Long.parseLong(userSeqDto.getUserSeq()));
        return ResponseEntity.ok(totalMoney.toString());
    }


}
