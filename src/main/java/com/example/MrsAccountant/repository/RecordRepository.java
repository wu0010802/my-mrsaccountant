package com.example.mrsaccountant.repository;

import com.example.mrsaccountant.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
// import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByUserId(Long userId);

    @Query("SELECT r FROM Record r WHERE r.user.id = :userId AND r.date BETWEEN :startDate AND :endDate")
    List<Record> findByUserIdAndDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
