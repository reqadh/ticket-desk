package com.example.ticketdesk.ticket;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TicketService {

    private final TicketRepository tickets;

    public TicketService(TicketRepository tickets) {
        this.tickets = tickets;
    }

    @Transactional
    public Ticket create(String title, Priority priority) {
        return tickets.save(new Ticket(title, priority));
    }

    public Ticket get(Long id) {
        return tickets.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket " + id + " not found"));
    }

    @Transactional
    public Ticket changeStatus(Long id, TicketStatus status) {
        Ticket ticket = get(id);
        ticket.changeStatus(status);
        return ticket;
    }
}
