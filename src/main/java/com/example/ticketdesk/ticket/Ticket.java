package com.example.ticketdesk.ticket;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class Ticket {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;

    private Instant createdAt = Instant.now();

    protected Ticket() {
    }

    public Ticket(String title, Priority priority) {
        this.title = title;
        this.priority = priority == null ? Priority.NORMAL : priority;
    }

    public void changeStatus(TicketStatus newStatus) {
        status = newStatus;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public TicketStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public Instant getCreatedAt() { return createdAt; }
}
