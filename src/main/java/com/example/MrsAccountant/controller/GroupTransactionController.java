package com.example.mrsaccountant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.service.GroupService;
import com.example.mrsaccountant.service.GroupTransactionService;

@RestController
@RequestMapping("/mrsaccountant")
public class GroupTransactionController {

    private final GroupService groupService;
    private final GroupTransactionService groupTransactionService;

    public GroupTransactionController(GroupService groupService, GroupTransactionService groupTransactionService) {
        this.groupService = groupService;
        this.groupTransactionService = groupTransactionService;
    }

    @GetMapping("/group/transaction")
    public ResponseEntity<?> getTransactionByGroupId(@RequestParam Long groupId) {
        List<GroupTransaction> groupTransactions = groupTransactionService.findGroupTransactionsByGroupId(groupId);
        return ResponseEntity.ok(groupTransactions);
    }

    @PostMapping("/group/transaction")
    public ResponseEntity<?> postTransactionByGroupId(@RequestParam Long groupId,
            @RequestBody GroupTransaction groupTransaction) {
        Group group = groupService.getGroupByGroupId(groupId);
        groupTransaction.setGroup(group);
        groupTransactionService.addGroupTransaction(groupTransaction);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("GroupTransaction added successfully!");
    }

}
