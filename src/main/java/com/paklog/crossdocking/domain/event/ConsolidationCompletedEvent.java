package com.paklog.crossdocking.domain.event;


public class ConsolidationCompletedEvent extends DomainEvent {
    private String consolidationId;
    private Integer itemsConsolidated;
    private String destinationDock;

    private ConsolidationCompletedEvent(String consolidationId, Integer itemsConsolidated, String destinationDock) {
        this.consolidationId = consolidationId;
        this.itemsConsolidated = itemsConsolidated;
        this.destinationDock = destinationDock;
    }

    public String getConsolidationId() { return consolidationId; }
    public Integer getItemsConsolidated() { return itemsConsolidated; }
    public String getDestinationDock() { return destinationDock; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String consolidationId;
        private Integer itemsConsolidated;
        private String destinationDock;

        public Builder consolidationId(String consolidationId) { this.consolidationId = consolidationId; return this; }
        public Builder itemsConsolidated(Integer itemsConsolidated) { this.itemsConsolidated = itemsConsolidated; return this; }
        public Builder destinationDock(String destinationDock) { this.destinationDock = destinationDock; return this; }

        public ConsolidationCompletedEvent build() {
            return new ConsolidationCompletedEvent(consolidationId, itemsConsolidated, destinationDock);
        
}
}
}
