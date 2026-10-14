package com.example.ticketdesk.notification;

import com.example.ticketdesk.ticket.Priority;

public record TicketNotification(Long ticketId, String title, Priority priority, String sound) {
}
