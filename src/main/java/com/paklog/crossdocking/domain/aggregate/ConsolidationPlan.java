package com.paklog.crossdocking.domain.aggregate;

import com.paklog.crossdocking.domain.event.*;
import com.paklog.crossdocking.domain.valueobject.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.*;

@Document(collection = "consolidation_plans")
public class ConsolidationPlan {
    
    @Id
    private String id;
    
    private String planNumber;
    private ConsolidationStrategy strategy;
    private String status;
    
    private List<String> sourceTransferIds;
    private String consolidatedDestination;
    
    private Integer totalItems;
    private Map<String, Integer> itemsBySource;
    
    private Instant startedAt;
    private Instant completedAt;
    
    @Version
    private Long version;
    
    @CreatedDate
    private Instant createdAt;
    
    @LastModifiedDate
    private Instant updatedAt;
    
    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();
    
    public void complete(int itemsConsolidated) {
        this.status = "COMPLETED";
        this.completedAt = Instant.now();
        this.totalItems = itemsConsolidated;
        
        addDomainEvent(ConsolidationCompletedEvent.builder()
            .consolidationId(this.id)
            .itemsConsolidated(itemsConsolidated)
            .destinationDock(this.consolidatedDestination)
            .build());
    }
    
    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() { return new ArrayList<>(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    // Getters
    public String getId() { return id; }
    public String getPlanNumber() { return planNumber; }
    public ConsolidationStrategy getStrategy() { return strategy; }
    public String getStatus() { return status; }
    public List<String> getSourceTransferIds() { return sourceTransferIds; }
    public String getConsolidatedDestination() { return consolidatedDestination; }
    public Integer getTotalItems() { return totalItems; }
    public Map<String, Integer> getItemsBySource() { return itemsBySource; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public Long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPlanNumber(String planNumber) { this.planNumber = planNumber; }
    public void setStrategy(ConsolidationStrategy strategy) { this.strategy = strategy; }
    public void setStatus(String status) { this.status = status; }
    public void setSourceTransferIds(List<String> sourceTransferIds) { this.sourceTransferIds = sourceTransferIds; }
    public void setConsolidatedDestination(String consolidatedDestination) { this.consolidatedDestination = consolidatedDestination; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }
    public void setItemsBySource(Map<String, Integer> itemsBySource) { this.itemsBySource = itemsBySource; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public void setVersion(Long version) { this.version = version; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String planNumber;
        private ConsolidationStrategy strategy;
        private String status;
        private List<String> sourceTransferIds;
        private String consolidatedDestination;
        private Integer totalItems;
        private Map<String, Integer> itemsBySource;
        private Instant startedAt;
        private Instant completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder planNumber(String planNumber) { this.planNumber = planNumber; return this; }
        public Builder strategy(ConsolidationStrategy strategy) { this.strategy = strategy; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder sourceTransferIds(List<String> sourceTransferIds) { this.sourceTransferIds = sourceTransferIds; return this; }
        public Builder consolidatedDestination(String consolidatedDestination) { this.consolidatedDestination = consolidatedDestination; return this; }
        public Builder totalItems(Integer totalItems) { this.totalItems = totalItems; return this; }
        public Builder itemsBySource(Map<String, Integer> itemsBySource) { this.itemsBySource = itemsBySource; return this; }
        public Builder startedAt(Instant startedAt) { this.startedAt = startedAt; return this; }
        public Builder completedAt(Instant completedAt) { this.completedAt = completedAt; return this; }

        public ConsolidationPlan build() {
            ConsolidationPlan plan = new ConsolidationPlan();
            plan.id = this.id;
            plan.planNumber = this.planNumber;
            plan.strategy = this.strategy;
            plan.status = this.status;
            plan.sourceTransferIds = this.sourceTransferIds;
            plan.consolidatedDestination = this.consolidatedDestination;
            plan.totalItems = this.totalItems;
            plan.itemsBySource = this.itemsBySource;
            plan.startedAt = this.startedAt;
            plan.completedAt = this.completedAt;
            return plan;
        

}
}
}
