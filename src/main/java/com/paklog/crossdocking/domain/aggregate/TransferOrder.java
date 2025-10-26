package com.paklog.crossdocking.domain.aggregate;

import com.paklog.crossdocking.domain.event.*;
import com.paklog.crossdocking.domain.valueobject.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.*;

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


    // Getters
    public String getId() { return id; }
    public String getTransferNumber() { return transferNumber; }
    public TransferType getTransferType() { return transferType; }
    public FlowStatus getStatus() { return status; }
    public String getSourceDockId() { return sourceDockId; }
    public String getDestinationDockId() { return destinationDockId; }
    public List<Map<String, Object>> getItems() { return items; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public TimingWindow getTimingWindow() { return timingWindow; }
    public TransferPriority getPriority() { return priority; }
    public String getInboundShipmentId() { return inboundShipmentId; }
    public String getOutboundShipmentId() { return outboundShipmentId; }
    public Instant getInitiatedAt() { return initiatedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public Long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTransferNumber(String transferNumber) { this.transferNumber = transferNumber; }
    public void setTransferType(TransferType transferType) { this.transferType = transferType; }
    public void setStatus(FlowStatus status) { this.status = status; }
    public void setSourceDockId(String sourceDockId) { this.sourceDockId = sourceDockId; }
    public void setDestinationDockId(String destinationDockId) { this.destinationDockId = destinationDockId; }
    public void setItems(List<Map<String, Object>> items) { this.items = items; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public void setTimingWindow(TimingWindow timingWindow) { this.timingWindow = timingWindow; }
    public void setPriority(TransferPriority priority) { this.priority = priority; }
    public void setInboundShipmentId(String inboundShipmentId) { this.inboundShipmentId = inboundShipmentId; }
    public void setOutboundShipmentId(String outboundShipmentId) { this.outboundShipmentId = outboundShipmentId; }
    public void setInitiatedAt(Instant initiatedAt) { this.initiatedAt = initiatedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public void setVersion(Long version) { this.version = version; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
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

        public Builder id(String id) { this.id = id; return this; }
        public Builder transferNumber(String transferNumber) { this.transferNumber = transferNumber; return this; }
        public Builder transferType(TransferType transferType) { this.transferType = transferType; return this; }
        public Builder status(FlowStatus status) { this.status = status; return this; }
        public Builder sourceDockId(String sourceDockId) { this.sourceDockId = sourceDockId; return this; }
        public Builder destinationDockId(String destinationDockId) { this.destinationDockId = destinationDockId; return this; }
        public Builder items(List<Map<String, Object>> items) { this.items = items; return this; }
        public Builder totalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; return this; }
        public Builder timingWindow(TimingWindow timingWindow) { this.timingWindow = timingWindow; return this; }
        public Builder priority(TransferPriority priority) { this.priority = priority; return this; }
        public Builder inboundShipmentId(String inboundShipmentId) { this.inboundShipmentId = inboundShipmentId; return this; }
        public Builder outboundShipmentId(String outboundShipmentId) { this.outboundShipmentId = outboundShipmentId; return this; }
        public Builder initiatedAt(Instant initiatedAt) { this.initiatedAt = initiatedAt; return this; }
        public Builder completedAt(Instant completedAt) { this.completedAt = completedAt; return this; }

        public TransferOrder build() {
            TransferOrder obj = new TransferOrder();
            obj.id = this.id;
            obj.transferNumber = this.transferNumber;
            obj.transferType = this.transferType;
            obj.status = this.status;
            obj.sourceDockId = this.sourceDockId;
            obj.destinationDockId = this.destinationDockId;
            obj.items = this.items;
            obj.totalQuantity = this.totalQuantity;
            obj.timingWindow = this.timingWindow;
            obj.priority = this.priority;
            obj.inboundShipmentId = this.inboundShipmentId;
            obj.outboundShipmentId = this.outboundShipmentId;
            obj.initiatedAt = this.initiatedAt;
            obj.completedAt = this.completedAt;
            return obj;
        

}
}
}
