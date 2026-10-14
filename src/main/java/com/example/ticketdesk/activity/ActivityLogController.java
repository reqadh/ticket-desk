package com.example.ticketdesk.activity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ActivityLogController {

    private final ActivityLogRepository repository;

    public ActivityLogController(ActivityLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/tickets/{id}/activity")
    public List<ActivityLog> forTicket(@PathVariable Long id) {
        return repository.findByTicketIdOrderByTimestampAscIdAsc(id);
    }
}
