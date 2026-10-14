package com.example.ticketdesk.note;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

/** Notes are append-only: there are no setters and no update/delete endpoints. */
@Entity
public class Note {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long ticketId;

    @Column(nullable = false, updatable = false, length = 2000)
    private String text;

    @Column(nullable = false, updatable = false)
    private String author;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Note() {
    }

    public Note(Long ticketId, String text, String author) {
        this.ticketId = ticketId;
        this.text = text;
        this.author = author;
    }

    public Long getId() { return id; }
    public Long getTicketId() { return ticketId; }
    public String getText() { return text; }
    public String getAuthor() { return author; }
    public Instant getCreatedAt() { return createdAt; }
}
