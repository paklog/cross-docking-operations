package com.paklog.crossdocking.domain.aggregate;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flow_optimizations")
public class FlowOptimization {
    
    @Id
    private String id;
    
    private String optimizationName;
    private String algorithm;
    
    private List<String> operationIds;
    private Map<String, Object> parameters;
    private Map<String, Object> results;
    
    private Double efficiencyScore;
    private Integer totalHandlingSteps;
    private Integer optimizedHandlingSteps;
    
    private Instant executedAt;
    
    @Version
    private Long version;
    
    @CreatedDate
    private Instant createdAt;
    
    public double calculateEfficiencyGain() {
        if (totalHandlingSteps == null || optimizedHandlingSteps == null) return 0.0;
        int reduction = totalHandlingSteps - optimizedHandlingSteps;
        return (reduction.doubleValue() / totalHandlingSteps.doubleValue()) * 100.0;
    }
}
