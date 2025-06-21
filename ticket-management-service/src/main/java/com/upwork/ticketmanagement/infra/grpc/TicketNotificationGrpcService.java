package com.upwork.ticketmanagement.infra.grpc;

import com.upwork.grpc.ticket.RedemptionNotificationRequest;
import com.upwork.grpc.ticket.RedemptionNotificationResponse;
import com.upwork.grpc.ticket.TicketNotificationServiceGrpc;
import com.upwork.ticketmanagement.domain.service.TicketService;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@GRpcService
@AllArgsConstructor
public class TicketNotificationGrpcService extends TicketNotificationServiceGrpc.TicketNotificationServiceImplBase {

    private final TicketService ticketService;

    @Override
    @Transactional
    public void notifyRedemption(RedemptionNotificationRequest request, StreamObserver<RedemptionNotificationResponse> responseObserver) {
        ticketService.processRedemption(request.getTicketId(), request.getRedeemedAtAttractionId(), LocalDateTime.parse(request.getRedemptionTime()));

        RedemptionNotificationResponse response = RedemptionNotificationResponse.newBuilder()
                .setSuccess(true).setMessage("Ticket status updated to REDEEMED").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}