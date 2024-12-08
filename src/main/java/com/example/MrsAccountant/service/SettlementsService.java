package com.example.mrsaccountant.service;

import org.springframework.stereotype.Service;

import com.example.mrsaccountant.entity.TransactionSplit;

import java.util.*;

@Service
public class SettlementsService {

    private final GroupService groupService;
    private final GroupTransactionService groupTransactionService;

    public SettlementsService(GroupService groupService, GroupTransactionService groupTransactionService) {
        this.groupService = groupService;
        this.groupTransactionService = groupTransactionService;
        
    }



    // public Map<String, Integer> settlementCalculate(){
    //     groupTransactionService.
        
    //     Map<String, Integer> resultMap = new HashMap<>();
        
        



    // }
}
