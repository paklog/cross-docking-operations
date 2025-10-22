package com.paklog.crossdocking.domain.event;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public abstract class DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredAt = Instant.now();
    private final String eventType = this.getClass().getSimpleName();
}
