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

    /** Status the ticket had right before it was closed; used when reopening. */
    @Enumerated(EnumType.STRING)
    private TicketStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;

    private String district;
    private String buildingNumber;
    private String shortCode;
    private String city;

    private Instant createdAt = Instant.now();

    protected Ticket() {
    }

    public Ticket(String title, Priority priority) {
        this.title = title;
        this.priority = priority == null ? Priority.NORMAL : priority;
    }

    public void changeStatus(TicketStatus newStatus) {
        if (newStatus == TicketStatus.CLOSED && status != TicketStatus.CLOSED) {
            previousStatus = status;
        }
        status = newStatus;
    }

    public void reopen() {
        status = previousStatus == null ? TicketStatus.OPEN : previousStatus;
        previousStatus = null;
    }

    public void setAddress(String district, String buildingNumber, String shortCode, String city) {
        this.district = district;
        this.buildingNumber = buildingNumber;
        this.shortCode = shortCode;
        this.city = city;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public TicketStatus getStatus() { return status; }
    public TicketStatus getPreviousStatus() { return previousStatus; }
    public Priority getPriority() { return priority; }
    public String getDistrict() { return district; }
    public String getBuildingNumber() { return buildingNumber; }
    public String getShortCode() { return shortCode; }
    public String getCity() { return city; }
    public Instant getCreatedAt() { return createdAt; }
}
