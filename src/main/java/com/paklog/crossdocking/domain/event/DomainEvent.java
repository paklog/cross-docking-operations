package com.paklog.crossdocking.domain.event;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredAt = Instant.now();
    private final String eventType = this.getClass().getSimpleName();

    public String getEventId() { return eventId; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getEventType() { return eventType; }
}
