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
}
