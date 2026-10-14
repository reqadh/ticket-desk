package com.example.ticketdesk.note;

import com.example.ticketdesk.common.CurrentUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/notes")
public class NoteController {

    public record AddNoteRequest(@NotBlank @Size(max = 2000) String text) {
    }

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note add(@PathVariable Long ticketId,
                    @Valid @RequestBody AddNoteRequest request,
                    @RequestHeader(value = CurrentUser.HEADER, required = false) String user) {
        return service.add(ticketId, request.text(), CurrentUser.resolve(user));
    }

    @GetMapping
    public List<Note> list(@PathVariable Long ticketId) {
        return service.list(ticketId);
    }
}
