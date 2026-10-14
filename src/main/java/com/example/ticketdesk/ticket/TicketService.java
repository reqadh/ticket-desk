package com.example.ticketdesk.ticket;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TicketService {

    private final TicketRepository tickets;
    private final ApplicationEventPublisher events;

    public TicketService(TicketRepository tickets, ApplicationEventPublisher events) {
        this.tickets = tickets;
        this.events = events;
    }

    @Transactional
    public Ticket create(String title, Priority priority) {
        Ticket ticket = tickets.save(new Ticket(title, priority));
        events.publishEvent(new TicketCreatedEvent(ticket.getId(), ticket.getTitle(), ticket.getPriority()));
        return ticket;
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
