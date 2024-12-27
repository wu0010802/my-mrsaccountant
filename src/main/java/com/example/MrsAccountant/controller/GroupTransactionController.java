package com.example.mrsaccountant.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mrsaccountant.dto.GroupTransactionDTO;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.service.GroupTransactionService;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/mrsaccountant")
public class GroupTransactionController {

    private final GroupTransactionService groupTransactionService;
    private final SimpMessagingTemplate messagingTemplate;

    public GroupTransactionController(GroupTransactionService groupTransactionService,
            SimpMessagingTemplate messagingTemplate) {
        this.groupTransactionService = groupTransactionService;
        this.messagingTemplate = messagingTemplate;
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
            Map<String, Object> message = new HashMap<>();
            message.put("groupId", groupId);
            message.put("message", "New transaction created");
            messagingTemplate.convertAndSend("/topic/transactions", message);

            return ResponseEntity.ok("GroupTransaction created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create GroupTransaction.");
        }
    }

    // update 須留意userRecord同步
    @PutMapping("/group/transaction/{transactionId}")
    public ResponseEntity<?> updateGroupTransaction(
            @PathVariable Long transactionId,
            @RequestBody GroupTransactionDTO updatedTransactionDTO) {
        try {
            groupTransactionService.updateGroupTransaction(transactionId, updatedTransactionDTO);
            return ResponseEntity.ok("GroupTransaction updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update GroupTransaction.");
        }
    }

    // delete 須留意userRecord同步

    @DeleteMapping("/group/transaction/{transactionId}")
    public ResponseEntity<?> deleteGroupTransaction(@PathVariable Long transactionId) {
        try {
            groupTransactionService.deleteGroupTransaction(transactionId);
            return ResponseEntity.ok("GroupTransaction deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete GroupTransaction.");
        }
    }



}
