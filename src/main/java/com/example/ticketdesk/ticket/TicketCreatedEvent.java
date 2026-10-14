package com.example.ticketdesk.ticket;

public record TicketCreatedEvent(Long ticketId, String title, Priority priority) {
}
