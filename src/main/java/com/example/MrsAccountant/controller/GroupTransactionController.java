package com.example.mrsaccountant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mrsaccountant.dto.GroupTransactionDTO;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.service.GroupTransactionService;


@RestController
@RequestMapping("/mrsaccountant")
public class GroupTransactionController {

    private final GroupTransactionService groupTransactionService;

    public GroupTransactionController(GroupTransactionService groupTransactionService) {
        this.groupTransactionService = groupTransactionService;
    }

    @GetMapping("/group/transaction")
    public ResponseEntity<?> getTransactionByGroupId(@RequestParam Long groupId) {
        List<GroupTransaction> groupTransactions = groupTransactionService.findGroupTransactionsByGroupId(groupId);
        return ResponseEntity.ok(groupTransactions);
    }

    @PostMapping("/group/transaction/{groupId}")
    public ResponseEntity<?> createGroupTransaction(
            @RequestBody GroupTransactionDTO groupTransactionDTO,
            @PathVariable Long groupId) {
        try {
            groupTransactionService.createGroupTransaction(groupTransactionDTO, groupId);
            return ResponseEntity.ok("GroupTransaction created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create GroupTransaction.");
        }
    }

    // update 須留意userRecord同步
    // delete 須留意userRecord同步
}

