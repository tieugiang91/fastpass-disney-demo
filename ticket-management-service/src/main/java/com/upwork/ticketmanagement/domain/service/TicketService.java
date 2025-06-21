package com.upwork.ticketmanagement.domain.service;

import com.upwork.ticketmanagement.domain.model.FastPassTicket;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TicketService {
    FastPassTicket issueTicket(String attractionId, String attractionName, int waitTimeMinutes);
    Optional<FastPassTicket> getTicketDetails(String ticketId);
    void processRedemption(String ticketId, String redeemedAtAttractionId, LocalDateTime redemptionTime);
}