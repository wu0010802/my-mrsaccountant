package com.example.mrsaccountant.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.mrsaccountant.dto.GroupTransactionDTO;
import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.entity.TransactionSplit;

import com.example.mrsaccountant.entity.TransactionSplit.Role;
import com.example.mrsaccountant.repository.GroupTransactionRespository;
import com.example.mrsaccountant.entity.Record;

@Service
public class GroupTransactionService {

    private final GroupTransactionRespository groupTransactionRespository;
    private final GroupService groupService;
    private final UserService userService;
    private final RecordService recordService;

    public GroupTransactionService(GroupTransactionRespository groupTransactionRespository,
            GroupService groupService,
            UserService userService, RecordService recordService) {
        this.groupTransactionRespository = groupTransactionRespository;
        this.groupService = groupService;
        this.userService = userService;
        this.recordService = recordService;
    }

    public List<GroupTransaction> findGroupTransactionsByGroupId(Long groupId) {
        return groupTransactionRespository.findGroupTransactionsByGroupId(groupId);
    }

    public void createGroupTransaction(GroupTransactionDTO groupTransactionDTO, Long groupId) {

        Group group = groupService.getGroupByGroupId(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        GroupTransaction groupTransaction = new GroupTransaction();
        groupTransaction.setAmount(groupTransactionDTO.getAmount());
        groupTransaction.setType(GroupTransaction.Type.valueOf(groupTransactionDTO.getType()));
        groupTransaction.setCategory(groupTransactionDTO.getCategory());
        groupTransaction.setDate(groupTransactionDTO.getDate());
        groupTransaction.setName(groupTransactionDTO.getName());
        groupTransaction.setGroup(group);

        List<Record> syncRecords = new ArrayList<>();
        List<TransactionSplit> splits = groupTransactionDTO.getTransactionSplits().stream().map(splitDTO -> {

            TransactionSplit split = new TransactionSplit();
            split.setAmount(splitDTO.getAmount());
            split.setRole(TransactionSplit.Role.valueOf(splitDTO.getRole()));
            split.setUser(userService.getUserById(splitDTO.getUserId()));
            split.setGroupTransaction(groupTransaction);

            if (split.getRole().equals(TransactionSplit.Role.RECEIVER)) {
                Record syncRecord = new Record();
                syncRecord.setAmount(split.getAmount());
                syncRecord.setCategory(groupTransaction.getCategory());
                syncRecord.setDate(groupTransaction.getDate());
                syncRecord.setUser(split.getUser());
                syncRecord.setName(groupTransaction.getName());
                syncRecord.setType(Record.Type.EXPENSE);
                syncRecord.setTransactionSplit(split);
                syncRecords.add(syncRecord);
            }

            return split;
        }).collect(Collectors.toList());

        groupTransaction.setTransactionSplits(splits);

        groupTransactionRespository.save(groupTransaction);

        syncRecords.forEach(recordService::saveRecord);
    }

    public void updateGroupTransaction(Long transactionId, GroupTransactionDTO updatedTransactionDTO) {

        
        GroupTransaction existingTransaction = groupTransactionRespository.findById(transactionId)
                .orElseThrow(
                        () -> new IllegalArgumentException("GroupTransaction not found with ID: " + transactionId));

       
        if (updatedTransactionDTO.getAmount() != null) {
            existingTransaction.setAmount(updatedTransactionDTO.getAmount());
        }
        if (updatedTransactionDTO.getCategory() != null) {
            existingTransaction.setCategory(updatedTransactionDTO.getCategory());
        }
        if (updatedTransactionDTO.getDate() != null) {
            existingTransaction.setDate(updatedTransactionDTO.getDate());
        }
        if (updatedTransactionDTO.getName() != null) {
            existingTransaction.setName(updatedTransactionDTO.getName());
        }
        if (updatedTransactionDTO.getType() != null) {
            existingTransaction.setType(GroupTransaction.Type.valueOf(updatedTransactionDTO.getType()));
        }

        List<TransactionSplit> updatedSplits = updatedTransactionDTO.getTransactionSplits().stream().map(splitDTO -> {

            TransactionSplit split = existingTransaction.getTransactionSplits().stream()
                    .filter(existingSplit -> existingSplit.getId() != null
                            && existingSplit.getId().equals(splitDTO.getId()))
                    .findFirst()
                    .orElse(new TransactionSplit());

            split.setAmount(splitDTO.getAmount());
            split.setRole(TransactionSplit.Role.valueOf(splitDTO.getRole()));
            split.setUser(userService.getUserById(splitDTO.getUserId()));
            split.setGroupTransaction(existingTransaction);

            return split;
        }).collect(Collectors.toList());

        existingTransaction.getTransactionSplits().clear();
        existingTransaction.getTransactionSplits().addAll(updatedSplits);

        List<Record> updatedRecords = updatedSplits.stream().filter(split -> split.getRole().equals(Role.RECEIVER))
                .map(split -> {

                    Record updatedRecord = split.getRecord();
                    if (updatedRecord == null) {
                        updatedRecord = new Record();
                    }
                    updatedRecord.setAmount(split.getAmount());
                    updatedRecord.setCategory(existingTransaction.getCategory());
                    updatedRecord.setDate(existingTransaction.getDate());
                    updatedRecord.setUser(split.getUser());
                    updatedRecord.setName(existingTransaction.getName());
                    updatedRecord.setType(Record.Type.EXPENSE);
                    updatedRecord.setTransactionSplit(split);
                    return updatedRecord;
                }).collect(Collectors.toList());

        recordService.deleteRecordsByTransactionId(transactionId);
        updatedRecords.forEach(recordService::saveRecord);

        groupTransactionRespository.save(existingTransaction);
    }

    public void deleteGroupTransaction(Long transactionId) {

        GroupTransaction existingTransaction = groupTransactionRespository.findById(transactionId)
                .orElseThrow(
                        () -> new IllegalArgumentException("GroupTransaction not found with ID: " + transactionId));

        recordService.deleteRecordsByTransactionId(transactionId);

        groupTransactionRespository.delete(existingTransaction);
    }

}
