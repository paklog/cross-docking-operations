package com.paklog.crossdocking.domain.aggregate;

import com.paklog.crossdocking.domain.event.*;
import com.paklog.crossdocking.domain.valueobject.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transfer_orders")
public class TransferOrder {
    
    @Id
    private String id;
    
    private String transferNumber;
    private TransferType transferType;
    private FlowStatus status;
    
    private String sourceDockId;
    private String destinationDockId;
    
    private List<Map<String, Object>> items;
    private Integer totalQuantity;
    
    private TimingWindow timingWindow;
    private TransferPriority priority;
    
    private String inboundShipmentId;
    private String outboundShipmentId;
    
    private Instant initiatedAt;
    private Instant completedAt;
    
    @Version
    private Long version;
    
    @CreatedDate
    private Instant createdAt;
    
    @LastModifiedDate
    private Instant updatedAt;
    
    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();
    
    public void initiate() {
        this.status = FlowStatus.IN_PROGRESS;
        this.initiatedAt = Instant.now();
        
        addDomainEvent(DirectTransferInitiatedEvent.builder()
            .transferOrderId(this.id)
            .sourceDock(this.sourceDockId)
            .destinationDock(this.destinationDockId)
            .initiatedAt(this.initiatedAt)
            .build());
    }
    
    public void complete() {
        this.status = FlowStatus.COMPLETED;
        this.completedAt = Instant.now();
    }
    
    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() { return new ArrayList<>(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }
}
