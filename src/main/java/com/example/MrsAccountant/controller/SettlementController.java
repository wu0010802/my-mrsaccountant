package com.example.mrsaccountant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mrsaccountant.dto.SettlementStatusDTO;
import com.example.mrsaccountant.entity.Settlement;
import com.example.mrsaccountant.service.SettlementsService;

@RestController
@RequestMapping("/mrsaccountant")
public class SettlementController {
    private final SettlementsService settlementsService;

    public SettlementController(SettlementsService settlementsService) {
        this.settlementsService = settlementsService;
    }

    @GetMapping("settlement")
    public ResponseEntity<?> testSettlement() {

        List<SettlementStatusDTO> settlementStatuses = settlementsService.replySettlement(1L);
        return ResponseEntity.ok(settlementStatuses);
    }

}
