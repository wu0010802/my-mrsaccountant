package com.example.mrsaccountant.controller;

import com.example.mrsaccountant.entity.Record;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.security.CustomUserDetails;
import com.example.mrsaccountant.service.RecordService;
import com.example.mrsaccountant.service.UserService;


import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mrsaccountant")
public class RecordController {

    private final RecordService recordService;
    private final UserService userService;

    public RecordController(RecordService recordService, UserService userService) {
        this.recordService = recordService;
        this.userService = userService;
    }
    // 返回用戶records根據年月日
    @GetMapping("/records")
    public ResponseEntity<?> getRecordsByUserId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) String datetype) {
        String userEmail = customUserDetails.getUsername();

        User user = userService.getUserByEmail(userEmail);

        List<Record> records;

        if (datetype != null) {

            records = recordService.findRecordsByUserIdAndDateType(user.getUserId(), datetype);
        } else {

            records = recordService.findRecordsByUserId(user.getUserId());
        }

        return ResponseEntity.ok(records);
    }

    @PostMapping("/records")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addRecord(@RequestBody Record record,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String userEmail = customUserDetails.getUsername();

        User user = userService.getUserByEmail(userEmail);
        record.setUser(user);
        recordService.saveRecord(record);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Record added successfully!");
    }

}
