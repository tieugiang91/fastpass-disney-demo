package com.upwork.ticketmanagement.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "fast_pass_tickets")
public class FastPassTicket {
    @Id
    private String ticketId;
    private String attractionId;
    private String attractionName;
    private LocalTime returnTimeStart;
    private LocalTime returnTimeEnd;
    private LocalDateTime issuedAt;
    private LocalDateTime redeemedAt;
    private String redeemedAtAttractionId;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    public static FastPassTicket issue(String attractionId, String attractionName, int waitTimeMinutes) {
        FastPassTicket ticket = new FastPassTicket();
        LocalTime now = LocalTime.now();
        ticket.ticketId = UUID.randomUUID().toString();
        ticket.attractionId = attractionId;
        ticket.attractionName = attractionName;
        ticket.returnTimeStart = now.plusMinutes(waitTimeMinutes);
        ticket.returnTimeEnd = now.plusMinutes(waitTimeMinutes).plusHours(1);
        ticket.issuedAt = LocalDateTime.now();
        ticket.status = TicketStatus.ISSUED;
        return ticket;
    }

    public void processRedemption(String redeemedAttraction, LocalDateTime redemptionTime) {
        if (this.status == TicketStatus.ISSUED) {
            this.status = TicketStatus.REDEEMED;
            this.redeemedAt = redemptionTime;
            this.redeemedAtAttractionId = redeemedAttraction;
        }
    }
}