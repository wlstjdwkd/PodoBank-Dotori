package com.yongy.dotori.domain.purposeData.repository;

import com.yongy.dotori.domain.purpose.entity.Purpose;
import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurposeDataRepository extends JpaRepository<PurposeData, Long> {
    List<PurposeData> findAllByPurpose(Purpose purpose);
}
