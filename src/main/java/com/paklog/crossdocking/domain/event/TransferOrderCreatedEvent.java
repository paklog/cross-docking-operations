package com.paklog.crossdocking.domain.event;

import com.paklog.crossdocking.domain.valueobject.TransferType;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TransferOrderCreatedEvent extends DomainEvent {
    private String transferOrderId;
    private TransferType transferType;
    private String sourceDock;
    private String destinationDock;
}
