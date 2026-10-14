package com.example.ticketdesk.notification;

import com.example.ticketdesk.ticket.TicketCreatedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@EnableConfigurationProperties(NotificationProperties.class)
public class NotificationService {

    private final NotificationProperties properties;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public NotificationService(NotificationProperties properties) {
        this.properties = properties;
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        return emitter;
    }

    /** Runs after the ticket is committed so listeners never hear about a rolled-back ticket. */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onTicketCreated(TicketCreatedEvent event) {
        broadcast(new TicketNotification(
                event.ticketId(), event.title(), event.priority(), properties.soundFor(event.priority())));
    }

    void broadcast(TicketNotification notification) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("ticket-created").data(notification));
            } catch (IOException | IllegalStateException e) {
                emitters.remove(emitter);
            }
        }
    }
}
