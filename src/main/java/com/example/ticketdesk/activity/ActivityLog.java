package com.example.ticketdesk.activity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "activity_log")
public class ActivityLog {

    @Id
    @GeneratedValue
    private Long id;

    /** Null for actions that are not tied to a ticket (e.g. address searches). */
    private Long ticketId;

    private String action;
    private String userName;
    private String details;
    private Instant timestamp = Instant.now();

    protected ActivityLog() {
    }

    public ActivityLog(Long ticketId, String action, String userName, String details) {
        this.ticketId = ticketId;
        this.action = action;
        this.userName = userName;
        this.details = details;
    }

    public Long getId() { return id; }
    public Long getTicketId() { return ticketId; }
    public String getAction() { return action; }
    public String getUserName() { return userName; }
    public String getDetails() { return details; }
    public Instant getTimestamp() { return timestamp; }
}
