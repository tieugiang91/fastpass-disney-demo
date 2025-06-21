package com.upwork.ticketmanagement.application.service;

import com.upwork.ticketmanagement.application.dto.IssueTicketRequest;
import com.upwork.ticketmanagement.domain.model.FastPassTicket;
import com.upwork.ticketmanagement.domain.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketApplicationService {
    private final TicketService ticketService;

    public FastPassTicket issueTicket(IssueTicketRequest request) {
        return ticketService.issueTicket(request.attractionId(), request.attractionName(), request.waitTimeMinutes());
    }

    public Optional<FastPassTicket> getTicketDetails(String ticketId) {
        return ticketService.getTicketDetails(ticketId);
    }
}