package com.paklog.crossdocking.domain.aggregate;

import com.paklog.crossdocking.domain.event.*;
import com.paklog.crossdocking.domain.valueobject.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.*;

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

    // Getters
    public String getId() { return id; }
    public String getOperationNumber() { return operationNumber; }
    public FlowStatus getStatus() { return status; }
    public TransferType getTransferType() { return transferType; }
    public List<String> getTransferOrderIds() { return transferOrderIds; }
    public String getSourceDockId() { return sourceDockId; }
    public String getDestinationDockId() { return destinationDockId; }
    public TimingWindow getTimingWindow() { return timingWindow; }
    public TransferPriority getPriority() { return priority; }
    public Integer getTotalItems() { return totalItems; }
    public Integer getProcessedItems() { return processedItems; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public Long getDurationMinutes() { return durationMinutes; }
    public Map<String, Object> getMetrics() { return metrics; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
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

        public Builder id(String id) { this.id = id; return this; }
        public Builder operationNumber(String operationNumber) { this.operationNumber = operationNumber; return this; }
        public Builder status(FlowStatus status) { this.status = status; return this; }
        public Builder transferType(TransferType transferType) { this.transferType = transferType; return this; }
        public Builder transferOrderIds(List<String> transferOrderIds) { this.transferOrderIds = transferOrderIds; return this; }
        public Builder sourceDockId(String sourceDockId) { this.sourceDockId = sourceDockId; return this; }
        public Builder destinationDockId(String destinationDockId) { this.destinationDockId = destinationDockId; return this; }
        public Builder timingWindow(TimingWindow timingWindow) { this.timingWindow = timingWindow; return this; }
        public Builder priority(TransferPriority priority) { this.priority = priority; return this; }
        public Builder totalItems(Integer totalItems) { this.totalItems = totalItems; return this; }

        public CrossDockOperation build() {
            CrossDockOperation op = new CrossDockOperation();
            op.id = this.id;
            op.operationNumber = this.operationNumber;
            op.status = this.status;
            op.transferType = this.transferType;
            op.transferOrderIds = this.transferOrderIds;
            op.sourceDockId = this.sourceDockId;
            op.destinationDockId = this.destinationDockId;
            op.timingWindow = this.timingWindow;
            op.priority = this.priority;
            op.totalItems = this.totalItems;
            return op;
        
}
}
}
