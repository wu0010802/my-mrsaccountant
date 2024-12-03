package com.example.mrsaccountant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mrsaccountant.entity.GroupTransaction;
// import com.example.mrsaccountant.security.CustomUserDetails;
import com.example.mrsaccountant.service.GroupService;


@RestController
@RequestMapping("/mrsaccountant")
public class GroupController {
    
    private final GroupService groupService;

    public GroupController(GroupService groupService){
        this.groupService = groupService;
    }

   
    @GetMapping("/group/transaction")
    public ResponseEntity<?> getTransactionByGroupId(@RequestParam(required = false) Long groupId){
        List<GroupTransaction> groupTransactions = groupService.findGroupTransactionsByGroupId(groupId);
        return ResponseEntity.ok(groupTransactions);
    }


}
