package com.example.ticketdesk.note;

import com.example.ticketdesk.activity.ActivityLogService;
import com.example.ticketdesk.ticket.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository notes;
    private final TicketService tickets;
    private final ActivityLogService activity;

    public NoteService(NoteRepository notes, TicketService tickets, ActivityLogService activity) {
        this.notes = notes;
        this.tickets = tickets;
        this.activity = activity;
    }

    @Transactional
    public Note add(Long ticketId, String text, String author) {
        tickets.get(ticketId);
        Note note = notes.save(new Note(ticketId, text.trim(), author));
        activity.record(ticketId, "NOTE_ADDED", author, "Note #" + note.getId());
        return note;
    }

    public List<Note> list(Long ticketId) {
        tickets.get(ticketId);
        return notes.findByTicketIdOrderByCreatedAtAscIdAsc(ticketId);
    }
}
