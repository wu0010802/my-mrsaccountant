package com.example.mrsaccountant.service;

import org.springframework.stereotype.Service;
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

        List<TransactionSplit> splits = groupTransactionDTO.getTransactionSplits().stream().map(splitDTO -> {
            TransactionSplit split = new TransactionSplit();
            split.setAmount(splitDTO.getAmount());
            split.setRole(TransactionSplit.Role.valueOf(splitDTO.getRole()));
            split.setUser(userService.getUserById(splitDTO.getUserId()));
            split.setGroupTransaction(groupTransaction);
            return split;
        }).collect(Collectors.toList());

        // 同時新增紀錄至userRecord

        List<Record> synsRecords = splits.stream().filter(split -> {
            return split.getRole().equals(Role.RECEIVER);

        }).map(split -> {
            Record syncRecord = new Record();

            syncRecord.setAmount(split.getAmount());
            syncRecord.setCategory(groupTransaction.getCategory());
            syncRecord.setDate(groupTransaction.getDate());
            syncRecord.setUser(split.getUser());
            syncRecord.setName(groupTransaction.getName());
            syncRecord.setType(com.example.mrsaccountant.entity.Record.Type.EXPENSE);
            return syncRecord;
        }).collect(Collectors.toList());

        groupTransaction.setTransactionSplits(splits);
        for (Record synsRecord : synsRecords) {
            recordService.saveRecord(synsRecord);
        }

        groupTransactionRespository.save(groupTransaction);
    }
}
