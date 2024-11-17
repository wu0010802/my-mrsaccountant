package com.example.mrsaccountant.repository;

import  com.example.mrsaccountant.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    
    List<Record> findByUser_UserId(Long userId);


    List<Record> findByType(Record.Type type);


    List<Record> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
