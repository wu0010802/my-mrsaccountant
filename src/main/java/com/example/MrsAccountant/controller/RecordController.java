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

  
    @GetMapping("/user/records")
    public ResponseEntity<?> getRecordsByUserId(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam(required = false) String startdate, 
        @RequestParam(required = false) String enddate){
        String userEmail = customUserDetails.getUsername();

        User user = userService.getUserByEmail(userEmail);

        List<Record> records;

        if (startdate != null || enddate != null) {

            records = recordService.findRecordsByUserIdAndDateType(user.getUserId(),startdate,enddate );
        } else {

            records = recordService.findRecordsByUserId(user.getUserId());
        }

        return ResponseEntity.ok(records);
    }

    @PostMapping("/user/records")
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

    @DeleteMapping("/user/records/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable("id") Long recordId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

            recordService.deleteRecord(recordId);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Record deleted successfully!");
    }

}
