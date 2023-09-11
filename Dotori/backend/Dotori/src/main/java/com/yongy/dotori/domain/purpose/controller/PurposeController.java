package com.yongy.dotori.domain.purpose.controller;

import com.yongy.dotori.domain.purpose.dto.PurposeDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeDetailDTO;
import com.yongy.dotori.domain.purpose.entity.Purpose;
import com.yongy.dotori.domain.purpose.service.PurposeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/purpose")
public class PurposeController {
    private PurposeServiceImpl purposeService;

    @PostMapping("/")
    public ResponseEntity<Void> createPurpose(@RequestBody PurposeDTO purposeDTO){
        // 새로운 목표 생성
        purposeService.createPurpose(purposeDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/")
    public ResponseEntity<List<Purpose>> findAllPurpose(){
        // 전체 목표 리스트 조회
        List<Purpose> purposesList = new ArrayList<>();




        return ResponseEntity.ok(purposesList);
    }


    @GetMapping("/{purposeId}")
    public ResponseEntity<PurposeDetailDTO> findPurposeDetail(@PathVariable("purposeId") Long purposeSeq){
        // 목표 조회
        // 로그인 된 사용자의 purpose가 맞는지 확인하는 작업 필요
        PurposeDetailDTO purposeDetailDTO = purposeService.findPurposeDetail(purposeSeq);
        return ResponseEntity.ok(purposeDetailDTO);
    }
}
