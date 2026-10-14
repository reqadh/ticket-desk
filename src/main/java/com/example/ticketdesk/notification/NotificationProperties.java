package com.example.ticketdesk.notification;

import com.example.ticketdesk.ticket.Priority;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;

@ConfigurationProperties(prefix = "notification")
public class NotificationProperties {

    private Map<Priority, String> sounds = new EnumMap<>(Priority.class);

    public Map<Priority, String> getSounds() {
        return sounds;
    }

    public void setSounds(Map<Priority, String> sounds) {
        this.sounds = sounds;
    }

    /** Falls back to the NORMAL sound when a priority has no sound configured. */
    public String soundFor(Priority priority) {
        String sound = priority == null ? null : sounds.get(priority);
        return sound != null ? sound : sounds.get(Priority.NORMAL);
    }
}
