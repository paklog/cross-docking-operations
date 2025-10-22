package com.paklog.crossdocking.domain.event;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class DirectTransferInitiatedEvent extends DomainEvent {
    private String transferOrderId;
    private String sourceDock;
    private String destinationDock;
    private java.time.Instant initiatedAt;
}
