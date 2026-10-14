package com.example.ticketdesk.ticket;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    public record CreateTicketRequest(@NotBlank String title, Priority priority) {
    }

    public record ChangeStatusRequest(@NotNull TicketStatus status) {
    }

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket create(@Valid @RequestBody CreateTicketRequest request) {
        return service.create(request.title(), request.priority());
    }

    @GetMapping("/{id}")
    public Ticket get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}/status")
    public Ticket changeStatus(@PathVariable Long id, @Valid @RequestBody ChangeStatusRequest request) {
        return service.changeStatus(id, request.status());
    }
}
