package com.example.mrsaccountant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.repository.GroupRespository;
import com.example.mrsaccountant.repository.GroupTransactionRespository;

@Service
public class GroupService {
    private final GroupRespository groupRespository;

    private final GroupTransactionRespository groupTransactionRespository;

    public GroupService(GroupRespository groupRespository,GroupTransactionRespository groupTransactionRespository){
        this.groupRespository = groupRespository;
        this.groupTransactionRespository = groupTransactionRespository;
    }


    public List<GroupTransaction> findGroupTransactionsByGroupId(Long GroupId){
        return groupTransactionRespository.findGroupTransactionsByGroupId(GroupId);
    }
}
