//package com.yongy.dotoripurposeservice.domain.account.repository;
//
//
//import com.yongy.dotoripurposeservice.domain.account.entity.Account;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface AccountRepository extends JpaRepository<Account, Long> {
//    Account findByAccountSeqAndDeleteAtIsNull(Long accountSeq);
//    Account findByAccountNumberAndDeleteAtIsNull(String AccountNumber);
//    List<Account> findAllByUserSeqAndDeleteAtIsNull(Long userSeq);
//}
