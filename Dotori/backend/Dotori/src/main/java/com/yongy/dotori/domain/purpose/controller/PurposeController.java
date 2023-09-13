package com.yongy.dotori.domain.purpose.controller;

import com.yongy.dotori.domain.purpose.dto.PurposeAllDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeDetailDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeSummaryDTO;
import com.yongy.dotori.domain.purpose.service.PurposeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/purpose")
public class PurposeController {
    private final PurposeServiceImpl purposeService;

    @Operation(summary = "새로운 목표 생성")
    @PostMapping("/")
    public ResponseEntity<Void> createPurpose(@RequestBody PurposeDTO purposeDTO){
        // 새로운 목표 생성
        purposeService.createPurpose(purposeDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "전체 목표 리스트 조회")
    @GetMapping("/")
    public ResponseEntity<PurposeAllDTO> findAllPurpose(){
        // 전체 목표 리스트 조회
        PurposeAllDTO result = purposeService.findAllPurpose();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "목표 상세 조회")
    @GetMapping("/{purposeSeq}")
    public ResponseEntity<PurposeDetailDTO> findPurposeDetail(@PathVariable("purposeSeq") Long purposeSeq){
        // 목표 조회
        // TODO 로그인 된 사용자의 purpose가 맞는지 확인하는 작업 필요
        PurposeDetailDTO purposeDetailDTO = purposeService.findPurposeDetail(purposeSeq);
        return ResponseEntity.ok(purposeDetailDTO);
    }


    @Operation(summary = "목표 중단")
    @PatchMapping("/terminate/{purposeSeq}")
    public ResponseEntity<Void> terminatePurpose(@PathVariable("purposeId") Long purposeSeq){
        // 목표 중단하기
        purposeService.terminatePurpose(purposeSeq);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "목표 진행 현황")
    @GetMapping("/terminate/{purposeSeq}")
    public ResponseEntity<PurposeSummaryDTO> terminateCheck(@PathVariable("purposeId") Long purposeSeq){
        // 목표 중단하기 눌렀을 때 현재 목표 진행 현황 보여줌
        PurposeSummaryDTO summary = purposeService.summarizePurpose(purposeSeq);
        return ResponseEntity.ok(summary);
    }
}
