package com.paklog.crossdocking.domain.event;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class CrossDockCompletedEvent extends DomainEvent {
    private String operationId;
    private Integer totalItems;
    private Long durationMinutes;
    private java.time.Instant completedAt;
}
