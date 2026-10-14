package com.example.ticketdesk.ticket;

import com.example.ticketdesk.activity.ActivityLogService;
import com.example.ticketdesk.common.CurrentUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ReopenController {

    public record ReopenRequest(@NotBlank String reason) {
    }

    private final TicketService tickets;
    private final ActivityLogService activity;

    public ReopenController(TicketService tickets, ActivityLogService activity) {
        this.tickets = tickets;
        this.activity = activity;
    }

    @PostMapping("/api/tickets/{id}/reopen")
    @Transactional
    public Ticket reopen(@PathVariable Long id,
                         @Valid @RequestBody ReopenRequest request,
                         @RequestHeader(value = CurrentUser.HEADER, required = false) String user) {
        Ticket ticket = tickets.get(id);
        if (ticket.getStatus() != TicketStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only CLOSED tickets can be reopened");
        }
        ticket.reopen();
        activity.record(id, "TICKET_REOPENED", CurrentUser.resolve(user), request.reason().trim());
        return ticket;
    }
}
