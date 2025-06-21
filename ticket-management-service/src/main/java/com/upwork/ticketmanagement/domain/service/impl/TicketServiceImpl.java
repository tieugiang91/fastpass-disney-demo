package com.upwork.ticketmanagement.domain.service.impl;

import com.upwork.ticketmanagement.domain.model.FastPassTicket;
import com.upwork.ticketmanagement.domain.repository.TicketRepository;
import com.upwork.ticketmanagement.domain.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public FastPassTicket issueTicket(String attractionId, String attractionName, int waitTimeMinutes) {
        FastPassTicket ticket = FastPassTicket.issue(attractionId, attractionName, waitTimeMinutes);
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FastPassTicket> getTicketDetails(String ticketId) {
        return ticketRepository.findById(ticketId);
    }

    @Override
    @Transactional
    public void processRedemption(String ticketId, String redeemedAtAttractionId, LocalDateTime redemptionTime) {
        FastPassTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalStateException("Ticket not found"));
        ticket.processRedemption(redeemedAtAttractionId, redemptionTime);
        ticketRepository.save(ticket);
    }
}