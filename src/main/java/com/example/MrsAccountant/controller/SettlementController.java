package com.example.mrsaccountant.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@GetMapping("settlement/{groupId}")
public ResponseEntity<?> getSettlementDetails(@PathVariable long groupId) {
    List<SettlementStatusDTO> settlementStatuses = settlementsService.replySettlement(groupId);
    List<Settlement> latestSettlements = settlementsService.getLatestSettlement(groupId);

    Map<String, Object> response = new HashMap<>();
    response.put("settlementStatuses", settlementStatuses);
    response.put("latestSettlements", latestSettlements);

    return ResponseEntity.ok(response);
}


}
