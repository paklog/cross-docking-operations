package com.paklog.crossdocking.infrastructure.persistence.document;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.Instant;
import java.util.*;

@Document(collection = "transfer_orders")
public class TransferOrderDocument {
    @Id
    private String id;
    @Indexed
    private String transferNumber;
    private String transferType;
    @Indexed
    private String status;
    private String sourceDockId;
    private String destinationDockId;
    private List<Map<String, Object>> items;
    private Integer totalQuantity;
    private Map<String, Object> timingWindow;
    private String priority;
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


    // Getters
    public String getId() { return id; }
    public String getTransferNumber() { return transferNumber; }
    public String getTransferType() { return transferType; }
    public String getStatus() { return status; }
    public String getSourceDockId() { return sourceDockId; }
    public String getDestinationDockId() { return destinationDockId; }
    public List<Map<String, Object>> getItems() { return items; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public Map<String, Object> getTimingWindow() { return timingWindow; }
    public String getPriority() { return priority; }
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
    public void setTransferType(String transferType) { this.transferType = transferType; }
    public void setStatus(String status) { this.status = status; }
    public void setSourceDockId(String sourceDockId) { this.sourceDockId = sourceDockId; }
    public void setDestinationDockId(String destinationDockId) { this.destinationDockId = destinationDockId; }
    public void setItems(List<Map<String, Object>> items) { this.items = items; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public void setTimingWindow(Map<String, Object> timingWindow) { this.timingWindow = timingWindow; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setInboundShipmentId(String inboundShipmentId) { this.inboundShipmentId = inboundShipmentId; }
    public void setOutboundShipmentId(String outboundShipmentId) { this.outboundShipmentId = outboundShipmentId; }
    public void setInitiatedAt(Instant initiatedAt) { this.initiatedAt = initiatedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public void setVersion(Long version) { this.version = version; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
