package com.paklog.crossdocking.domain.aggregate;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.*;

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
        return ((double) reduction / totalHandlingSteps.doubleValue()) * 100.0;
    }


    // Getters
    public String getId() { return id; }
    public String getOptimizationName() { return optimizationName; }
    public String getAlgorithm() { return algorithm; }
    public List<String> getOperationIds() { return operationIds; }
    public Map<String, Object> getParameters() { return parameters; }
    public Map<String, Object> getResults() { return results; }
    public Double getEfficiencyScore() { return efficiencyScore; }
    public Integer getTotalHandlingSteps() { return totalHandlingSteps; }
    public Integer getOptimizedHandlingSteps() { return optimizedHandlingSteps; }
    public Instant getExecutedAt() { return executedAt; }
    public Long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setOptimizationName(String optimizationName) { this.optimizationName = optimizationName; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    public void setOperationIds(List<String> operationIds) { this.operationIds = operationIds; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public void setResults(Map<String, Object> results) { this.results = results; }
    public void setEfficiencyScore(Double efficiencyScore) { this.efficiencyScore = efficiencyScore; }
    public void setTotalHandlingSteps(Integer totalHandlingSteps) { this.totalHandlingSteps = totalHandlingSteps; }
    public void setOptimizedHandlingSteps(Integer optimizedHandlingSteps) { this.optimizedHandlingSteps = optimizedHandlingSteps; }
    public void setExecutedAt(Instant executedAt) { this.executedAt = executedAt; }
    public void setVersion(Long version) { this.version = version; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
