package com.upwork.attractionredemption.infra.client.impl;

import com.upwork.attractionredemption.infra.client.TicketManagementClient;
import com.upwork.grpc.ticket.*;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TicketManagementGrpcClient implements TicketManagementClient {

    @GrpcClient("ticket-management-service")
    private TicketDetailsServiceGrpc.TicketDetailsServiceBlockingStub detailsClient;

    @GrpcClient("ticket-management-service")
    private TicketNotificationServiceGrpc.TicketNotificationServiceBlockingStub notificationClient;

    @Override
    public TicketDetailsResponse getTicketDetails(String ticketId) {
        try {
            TicketDetailsRequest request = TicketDetailsRequest.newBuilder().setTicketId(ticketId).build();
            return detailsClient.getTicketDetails(request);
        } catch (StatusRuntimeException e) {
            log.error("gRPC call to getTicketDetails failed: {}", e.getStatus());
            throw new IllegalStateException("Failed to communicate with Ticket Management Service.", e);
        }
    }

    @Override
    public void notifyRedemption(String ticketId, String attractionId, LocalDateTime redemptionTime) {
        try {
            RedemptionNotificationRequest request = RedemptionNotificationRequest.newBuilder()
                    .setTicketId(ticketId)
                    .setRedeemedAtAttractionId(attractionId)
                    .setRedemptionTime(redemptionTime.toString())
                    .build();
            RedemptionNotificationResponse response = notificationClient.notifyRedemption(request);
            if(!response.getSuccess()){
                throw new IllegalStateException("Ticket redemption notification failed: " + response.getMessage());
            }
        } catch (StatusRuntimeException e) {
            log.error("gRPC call to notifyRedemption failed: {}", e.getStatus());
            throw new IllegalStateException("Failed to communicate with Ticket Management Service.", e);
        }
    }
}