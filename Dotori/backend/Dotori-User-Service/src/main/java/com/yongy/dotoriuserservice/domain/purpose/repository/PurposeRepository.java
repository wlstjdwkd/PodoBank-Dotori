package com.yongy.dotoriuserservice.domain.purpose.repository;


import com.yongy.dotoriuserservice.domain.purpose.entity.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Long> {
    List<Purpose> findAllByUserIdAndTerminatedAtIsNull(String id);
}
