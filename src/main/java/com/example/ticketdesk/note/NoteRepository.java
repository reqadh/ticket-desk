package com.example.ticketdesk.note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByTicketIdOrderByCreatedAtAscIdAsc(Long ticketId);
}
