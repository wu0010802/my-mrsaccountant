package com.example.mrsaccountant.service;

import org.springframework.stereotype.Service;

import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.entity.Setttlement;
import com.example.mrsaccountant.entity.TransactionSplit;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.repository.SettlementRepository;

import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SettlementsService {

    private final GroupService groupService;
    
    private final SettlementRepository settlementRepository;

    public SettlementsService(GroupService groupService,
            SettlementRepository settlementRepository) {
        this.groupService = groupService;
        
        this.settlementRepository = settlementRepository;

    }

    @Transactional
    public void addSettlement(long groupId) {
        Group group = groupService.getGroupByGroupId(groupId);
        List<GroupTransaction> transactions = group.getGroupTransactions();
        Set<User> users = group.getBelongUsers();

        List<TransactionSplit> transactionSplits = transactions.stream()
                .filter(transaction -> transaction.getType().equals(GroupTransaction.Type.EXPENSE))
                .flatMap(transaction -> transaction.getTransactionSplits().stream())
                .collect(Collectors.toList());

        List<Setttlement> setttlements = users.stream()
                .map(user -> {
                    Setttlement settlement = new Setttlement();

                    double payAmount = transactionSplits.stream()
                            .filter(split -> split.getUser().equals(user)
                                    && split.getRole().equals(TransactionSplit.Role.PAYER))

                            .mapToDouble(TransactionSplit::getAmount)
                            .sum();
                    double receiveAmount = transactionSplits.stream()
                            .filter(split -> split.getUser().equals(user)
                                    && split.getRole().equals(TransactionSplit.Role.RECEIVER))
                            .mapToDouble(TransactionSplit::getAmount)
                            .sum();

                    double balanceAmount = payAmount - receiveAmount;

                    settlement.setPayAmount(payAmount);
                    settlement.setReceiveAmount(receiveAmount);
                    settlement.setBalanceAmount(balanceAmount);
                    settlement.setGroup(group);
                    settlement.setUser(user);

                    return settlement;
                })
                .collect(Collectors.toList());

        settlementRepository.saveAll(setttlements);

    }


 

}
