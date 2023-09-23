package com.yongy.dotori.domain.purpose.repository;

import com.yongy.dotori.domain.purpose.entity.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Long> {
    Purpose findByPurposeSeq(Long purposeSeq);
    List<Purpose> findAllByUserUserSeq(Long userSeq);

    List<Purpose> findAllByUserUserSeqAndTerminatedIsFalse(Long userSeq);

}
