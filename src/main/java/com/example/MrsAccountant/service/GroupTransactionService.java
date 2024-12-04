package com.example.mrsaccountant.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.repository.GroupTransactionRespository;

@Service
public class GroupTransactionService {

    private final GroupTransactionRespository groupTransactionRespository;

    public GroupTransactionService(GroupTransactionRespository groupTransactionRespository) {
        this.groupTransactionRespository = groupTransactionRespository;
    }

    public List<GroupTransaction> findGroupTransactionsByGroupId(Long GroupId) {
        return groupTransactionRespository.findGroupTransactionsByGroupId(GroupId);
    }

    public void addGroupTransaction(GroupTransaction groupTransaction) {
        groupTransactionRespository.save(groupTransaction);
    }
}
