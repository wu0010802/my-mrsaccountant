package com.example.mrsaccountant.repository;

import com.example.mrsaccountant.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByUserId(Long userId);

    @Query("SELECT r FROM Record r WHERE r.user.id = :userId AND r.date BETWEEN :startDate AND :endDate")
    List<Record> findByUserIdAndDateBetween(@Param("userId") Long userId, @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query("SELECT r FROM Record r WHERE r.transactionSplit.id = :transactionId")
    List<Record> findByTransactionId(@Param("transactionId") Long transactionId);

    List<Record> findByTransactionSplitId(Long transactionSplitId);
}

