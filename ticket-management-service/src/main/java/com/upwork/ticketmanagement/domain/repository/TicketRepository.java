package com.upwork.ticketmanagement.domain.repository;

import com.upwork.ticketmanagement.domain.model.FastPassTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<FastPassTicket, String> {}
