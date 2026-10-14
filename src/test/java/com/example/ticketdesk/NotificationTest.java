package com.example.ticketdesk;

import com.example.ticketdesk.notification.NotificationProperties;
import com.example.ticketdesk.notification.TicketNotification;
import com.example.ticketdesk.ticket.Priority;
import com.example.ticketdesk.ticket.TicketCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RecordApplicationEvents
class NotificationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    NotificationProperties properties;

    @Autowired
    ApplicationEvents events;

    @Test
    void eachPriorityHasADifferentSound() {
        assertThat(properties.soundFor(Priority.URGENT)).isEqualTo("/sounds/urgent.wav");
        assertThat(properties.soundFor(Priority.NORMAL)).isEqualTo("/sounds/normal.wav");
        assertThat(properties.soundFor(Priority.LOW)).isEqualTo("/sounds/low.wav");
    }

    @Test
    void unknownPriorityFallsBackToNormalSound() {
        NotificationProperties partial = new NotificationProperties();
        Map<Priority, String> sounds = new EnumMap<>(Priority.class);
        sounds.put(Priority.NORMAL, "/sounds/normal.wav");
        partial.setSounds(sounds);

        assertThat(partial.soundFor(Priority.URGENT)).isEqualTo("/sounds/normal.wav");
        assertThat(partial.soundFor(null)).isEqualTo("/sounds/normal.wav");
    }

    @Test
    void creatingTicketPublishesEventWithPriority() throws Exception {
        long id = TestSupport.createTicket(mvc, "URGENT");

        assertThat(events.stream(TicketCreatedEvent.class))
                .anySatisfy(e -> {
                    assertThat(e.ticketId()).isEqualTo(id);
                    assertThat(e.priority()).isEqualTo(Priority.URGENT);
                });
    }

    @Test
    void notificationCarriesPriorityAndSound() {
        var n = new TicketNotification(1L, "t", Priority.LOW, properties.soundFor(Priority.LOW));
        assertThat(n.sound()).isEqualTo("/sounds/low.wav");
    }

    @Test
    void streamEndpointOpensEventStream() throws Exception {
        mvc.perform(get("/api/notifications/stream"))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted());
    }

    @Test
    void soundFilesAreServed() throws Exception {
        for (String name : new String[]{"urgent", "normal", "low"}) {
            mvc.perform(get("/sounds/" + name + ".wav")).andExpect(status().isOk());
        }
    }
}
