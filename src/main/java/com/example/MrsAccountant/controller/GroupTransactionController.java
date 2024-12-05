package com.example.mrsaccountant.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.entity.TransactionSplit;
import com.example.mrsaccountant.service.GroupService;
import com.example.mrsaccountant.service.GroupTransactionService;
import com.example.mrsaccountant.service.UserService;

@RestController
@RequestMapping("/mrsaccountant")
public class GroupTransactionController {

    private final GroupService groupService;
    private final GroupTransactionService groupTransactionService;
    private final UserService userService;

    public GroupTransactionController(GroupService groupService, GroupTransactionService groupTransactionService,
            UserService userService) {
        this.groupService = groupService;
        this.groupTransactionService = groupTransactionService;
        this.userService = userService;
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

            Group group = groupService.getGroupByGroupId(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found with ID: " + groupId);
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

            groupTransaction.setTransactionSplits(splits);

            groupTransactionService.addGroupTransaction(groupTransaction);
            
            return ResponseEntity.ok("GroupTransaction created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create GroupTransaction.");
        }
    }

}
