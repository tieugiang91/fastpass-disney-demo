package com.upwork.attractionredemption.infra.controller;

import com.upwork.attractionredemption.application.service.RedemptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redemptions")
@AllArgsConstructor
public class RedemptionController {
    private final RedemptionService redemptionService;

    @PostMapping("/{attractionId}/{ticketId}")
    public ResponseEntity<String> redeemTicket(
            @PathVariable String attractionId,
            @PathVariable String ticketId
    ) {
        try {
            redemptionService.redeem(ticketId, attractionId);
            return ResponseEntity.ok("Ticket Redeemed Successfully via gRPC.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}