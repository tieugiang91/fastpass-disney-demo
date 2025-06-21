package com.upwork.ticketmanagement.infra.controller;

import com.upwork.ticketmanagement.application.dto.IssueTicketRequest;
import com.upwork.ticketmanagement.application.service.TicketApplicationService;
import com.upwork.ticketmanagement.domain.model.FastPassTicket;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketController {
    private final TicketApplicationService ticketApplicationService;

    @PostMapping
    public FastPassTicket issueTicket(@RequestBody IssueTicketRequest request) {
        return ticketApplicationService.issueTicket(request);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<FastPassTicket> getTicketDetails(@PathVariable String ticketId) {
        return ticketApplicationService.getTicketDetails(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}