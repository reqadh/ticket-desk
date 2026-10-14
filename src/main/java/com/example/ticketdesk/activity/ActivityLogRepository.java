package com.example.ticketdesk.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findByTicketIdOrderByTimestampAscIdAsc(Long ticketId);

    List<ActivityLog> findByActionOrderByIdAsc(String action);
}
