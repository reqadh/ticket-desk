package com.example.ticketdesk.common;

/**
 * No real authentication in this sample: the caller passes their name in the
 * X-User header and we fall back to a default agent name.
 */
public final class CurrentUser {

    public static final String HEADER = "X-User";
    public static final String DEFAULT = "agent";

    private CurrentUser() {
    }

    public static String resolve(String headerValue) {
        return (headerValue == null || headerValue.isBlank()) ? DEFAULT : headerValue.trim();
    }
}
