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
@Document(collection = "crossdock_operations")
public class CrossDockOperation {
    
    @Id
    private String id;
    
    private String operationNumber;
    private FlowStatus status;
    private TransferType transferType;
    
    private List<String> transferOrderIds;
    private String sourceDockId;
    private String destinationDockId;
    
    private TimingWindow timingWindow;
    private TransferPriority priority;
    
    private Integer totalItems;
    private Integer processedItems;
    
    private Instant startedAt;
    private Instant completedAt;
    private Long durationMinutes;
    
    private Map<String, Object> metrics;
    
    @Version
    private Long version;
    
    @CreatedDate
    private Instant createdAt;
    
    @LastModifiedDate
    private Instant updatedAt;
    
    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();
    
    public void start() {
        this.status = FlowStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
    }
    
    public void receive() {
        this.status = FlowStatus.RECEIVING;
    }
    
    public void sort() {
        this.status = FlowStatus.SORTING;
    }
    
    public void ship() {
        this.status = FlowStatus.SHIPPING;
    }
    
    public void complete() {
        this.status = FlowStatus.COMPLETED;
        this.completedAt = Instant.now();
        
        if (this.startedAt != null) {
            this.durationMinutes = (completedAt.toEpochMilli() - startedAt.toEpochMilli()) / 60000;
        }
        
        addDomainEvent(CrossDockCompletedEvent.builder()
            .operationId(this.id)
            .totalItems(this.totalItems)
            .durationMinutes(this.durationMinutes)
            .completedAt(this.completedAt)
            .build());
    }
    
    public void updateProgress(int itemsProcessed) {
        this.processedItems = itemsProcessed;
    }
    
    public double getCompletionPercentage() {
        if (totalItems == null || totalItems == 0) return 0.0;
        if (processedItems == null) return 0.0;
        return (processedItems.doubleValue() / totalItems.doubleValue()) * 100.0;
    }
    
    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() { return new ArrayList<>(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }
}
