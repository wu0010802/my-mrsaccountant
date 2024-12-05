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


    // public GroupTransaction save(GroupTransaction groupTransaction) {
    //     // 檢查 groupTransaction 是否已存在（通過 ID）
    //     if (groupTransaction.getId() != null) {
    //         GroupTransaction existingTransaction = groupTransactionRespository.findById(groupTransaction.getId())
    //                 .orElse(null);

    //         if (existingTransaction != null) {
    //             // 更新已有的 GroupTransaction
    //             existingTransaction.setAmount(groupTransaction.getAmount());
    //             existingTransaction.setType(groupTransaction.getType());
    //             existingTransaction.setCategory(groupTransaction.getCategory());
    //             existingTransaction.setDate(groupTransaction.getDate());
    //             existingTransaction.setName(groupTransaction.getName());

    //             // 更新相關聯的 TransactionSplits
    //             existingTransaction.getTransactionSplits().clear();
    //             existingTransaction.getTransactionSplits().addAll(groupTransaction.getTransactionSplits());
    //             existingTransaction.getTransactionSplits()
    //                     .forEach(split -> split.setGroupTransaction(existingTransaction));

    //             return groupTransactionRespository.save(existingTransaction);
    //         }
    //     }

    //     // 如果不存在，則新增一個新的 GroupTransaction
    //     return groupTransactionRespository.save(groupTransaction);
    // }


    
}
