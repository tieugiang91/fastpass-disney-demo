package com.upwork.ticketmanagement.infra.grpc;

import com.upwork.grpc.ticket.TicketDetailsRequest;
import com.upwork.grpc.ticket.TicketDetailsResponse;
import com.upwork.grpc.ticket.TicketDetailsServiceGrpc;
import com.upwork.ticketmanagement.domain.model.FastPassTicket;
import com.upwork.ticketmanagement.domain.service.TicketService;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@AllArgsConstructor
public class TicketDetailsGrpcService extends TicketDetailsServiceGrpc.TicketDetailsServiceImplBase {
    private final TicketService ticketService;

    @Override
    public void getTicketDetails(TicketDetailsRequest request, StreamObserver<TicketDetailsResponse> responseObserver) {
        FastPassTicket ticket = ticketService.getTicketDetails(request.getTicketId())
                .orElseThrow(() -> new IllegalStateException("Ticket not found"));

        TicketDetailsResponse response = TicketDetailsResponse.newBuilder()
                .setTicketId(ticket.getTicketId())
                .setAttractionId(ticket.getAttractionId())
                .setReturnTimeStart(ticket.getReturnTimeStart().toString())
                .setReturnTimeEnd(ticket.getReturnTimeEnd().toString())
                .setStatus(ticket.getStatus().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}