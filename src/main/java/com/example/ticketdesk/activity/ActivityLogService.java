package com.example.ticketdesk.activity;

import org.springframework.stereotype.Service;

@Service
public class ActivityLogService {

    private final ActivityLogRepository repository;

    public ActivityLogService(ActivityLogRepository repository) {
        this.repository = repository;
    }

    public void record(Long ticketId, String action, String userName, String details) {
        repository.save(new ActivityLog(ticketId, action, userName, details));
    }
}
