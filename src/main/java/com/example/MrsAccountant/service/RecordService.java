package com.example.mrsaccountant.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.mrsaccountant.entity.Record;
import com.example.mrsaccountant.repository.RecordRepository;

@Service
public class RecordService {

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

    public List<Record> findRecordsByUserIdAndDateType(Long userId, String startDate, String endDate) {

        return recordRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    public void saveRecord(Record record) {
        recordRepository.save(record);
    }

    public void updateRecord(Long recordId, Record updatedRecord) {

        Record existingRecord = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found with ID: " + recordId));

        if (updatedRecord.getAmount() != null && !updatedRecord.getAmount().isNaN()) {
            existingRecord.setAmount(updatedRecord.getAmount());
        }
        if (updatedRecord.getName() != null && !updatedRecord.getName().isEmpty()) {
            existingRecord.setName(updatedRecord.getName());
        }
        if (updatedRecord.getCategory() != null && !updatedRecord.getCategory().isEmpty()) {
            existingRecord.setCategory(updatedRecord.getCategory());
        }
        if (updatedRecord.getDate() != null && !updatedRecord.getDate().isEmpty()) {
            existingRecord.setDate(updatedRecord.getDate());
        }
        if (updatedRecord.getType() != null) {
            existingRecord.setType(updatedRecord.getType());
        }

        recordRepository.save(existingRecord);
    }

    public void deleteRecord(Long recordId) {
        recordRepository.deleteById(recordId);
    }

    public void deleteRecordsByTransactionId(Long transactionId) {
        List<Record> recordsToDelete = recordRepository.findByTransactionId(transactionId);
        recordRepository.deleteAll(recordsToDelete);
    }

}
