package com.paklog.crossdocking.domain.event;


public class DirectTransferInitiatedEvent extends DomainEvent {
    private String transferOrderId;
    private String sourceDock;
    private String destinationDock;
    private java.time.Instant initiatedAt;

    private DirectTransferInitiatedEvent(String transferOrderId, String sourceDock, String destinationDock, java.time.Instant initiatedAt) {
        this.transferOrderId = transferOrderId;
        this.sourceDock = sourceDock;
        this.destinationDock = destinationDock;
        this.initiatedAt = initiatedAt;
    }

    public String getTransferOrderId() { return transferOrderId; }
    public String getSourceDock() { return sourceDock; }
    public String getDestinationDock() { return destinationDock; }
    public java.time.Instant getInitiatedAt() { return initiatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String transferOrderId;
        private String sourceDock;
        private String destinationDock;
        private java.time.Instant initiatedAt;

        public Builder transferOrderId(String transferOrderId) { this.transferOrderId = transferOrderId; return this; }
        public Builder sourceDock(String sourceDock) { this.sourceDock = sourceDock; return this; }
        public Builder destinationDock(String destinationDock) { this.destinationDock = destinationDock; return this; }
        public Builder initiatedAt(java.time.Instant initiatedAt) { this.initiatedAt = initiatedAt; return this; }

        public DirectTransferInitiatedEvent build() {
            return new DirectTransferInitiatedEvent(transferOrderId, sourceDock, destinationDock, initiatedAt);
        
}
}
}
