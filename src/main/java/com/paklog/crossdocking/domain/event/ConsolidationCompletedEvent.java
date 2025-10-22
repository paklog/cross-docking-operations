package com.paklog.crossdocking.domain.event;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ConsolidationCompletedEvent extends DomainEvent {
    private String consolidationId;
    private Integer itemsConsolidated;
    private String destinationDock;
}
