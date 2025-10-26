package com.paklog.crossdocking.domain.event;

public class CrossDockCompletedEvent extends DomainEvent {
    private String operationId;
    private Integer totalItems;
    private Long durationMinutes;
    private java.time.Instant completedAt;

    private CrossDockCompletedEvent(String operationId, Integer totalItems, Long durationMinutes, java.time.Instant completedAt) {
        this.operationId = operationId;
        this.totalItems = totalItems;
        this.durationMinutes = durationMinutes;
        this.completedAt = completedAt;
    }

    public String getOperationId() { return operationId; }
    public Integer getTotalItems() { return totalItems; }
    public Long getDurationMinutes() { return durationMinutes; }
    public java.time.Instant getCompletedAt() { return completedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String operationId;
        private Integer totalItems;
        private Long durationMinutes;
        private java.time.Instant completedAt;

        public Builder operationId(String operationId) { this.operationId = operationId; return this; }
        public Builder totalItems(Integer totalItems) { this.totalItems = totalItems; return this; }
        public Builder durationMinutes(Long durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public Builder completedAt(java.time.Instant completedAt) { this.completedAt = completedAt; return this; }

        public CrossDockCompletedEvent build() {
            return new CrossDockCompletedEvent(operationId, totalItems, durationMinutes, completedAt);
        
}
}
}
