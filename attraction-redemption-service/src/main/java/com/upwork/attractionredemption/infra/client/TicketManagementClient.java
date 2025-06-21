package com.upwork.attractionredemption.infra.client;

import com.upwork.grpc.ticket.TicketDetailsResponse;

import java.time.LocalDateTime;

public interface TicketManagementClient {
    TicketDetailsResponse getTicketDetails(String ticketId);
    void notifyRedemption(String ticketId, String attractionId, LocalDateTime redemptionTime);
}
