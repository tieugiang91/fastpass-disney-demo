package com.upwork.attractionredemption.application.service;

import com.upwork.attractionredemption.infra.client.TicketManagementClient;
import com.upwork.grpc.ticket.TicketDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
@AllArgsConstructor
public class RedemptionService {

    private final TicketManagementClient ticketManagementClient;

    public void redeem(String ticketId, String currentAttractionId) {
        TicketDetailsResponse ticket = ticketManagementClient.getTicketDetails(ticketId);
        log.info("Fetched ticket details via gRPC for ticket: {}", ticket.getTicketId());

        if (!"ISSUED".equals(ticket.getStatus())) {
            throw new IllegalStateException("Ticket already redeemed or invalid.");
        }
        if (!ticket.getAttractionId().equals(currentAttractionId)) {
            throw new IllegalStateException("Ticket is for a different attraction.");
        }
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.parse(ticket.getReturnTimeStart())) || now.isAfter(LocalTime.parse(ticket.getReturnTimeEnd()))) {
            throw new IllegalStateException("Not within the valid return window.");
        }

        ticketManagementClient.notifyRedemption(ticketId, currentAttractionId, LocalDateTime.now());
        log.info("Successfully notified Ticket Management Service for ticket redemption: {}", ticketId);
    }
}