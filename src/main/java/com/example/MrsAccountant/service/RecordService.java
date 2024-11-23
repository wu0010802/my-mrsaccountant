package com.example.mrsaccountant.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.mrsaccountant.entity.Record;
import com.example.mrsaccountant.repository.RecordRepository;
import java.time.LocalDate;

@Service
public class 
RecordService {

    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<Record> findRecordsByUserId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        return recordRepository.findByUserId(id);
    }

    public List<Record> findRecordsByUserIdAndDateType(Long userId, String startDate,String endDate) {
    

        return recordRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    public void saveRecord(Record record) {
        recordRepository.save(record);
    }

    public void deleteRecord(Long recordId){
        recordRepository.deleteById(recordId);
    }

}
