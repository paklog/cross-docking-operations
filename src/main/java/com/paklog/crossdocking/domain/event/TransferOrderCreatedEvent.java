package com.paklog.crossdocking.domain.event;

import com.paklog.crossdocking.domain.valueobject.TransferType;

public class TransferOrderCreatedEvent extends DomainEvent {
    private String transferOrderId;
    private TransferType transferType;
    private String sourceDock;
    private String destinationDock;

    private TransferOrderCreatedEvent(String transferOrderId, TransferType transferType, String sourceDock, String destinationDock) {
        this.transferOrderId = transferOrderId;
        this.transferType = transferType;
        this.sourceDock = sourceDock;
        this.destinationDock = destinationDock;
    }

    public String getTransferOrderId() { return transferOrderId; }
    public TransferType getTransferType() { return transferType; }
    public String getSourceDock() { return sourceDock; }
    public String getDestinationDock() { return destinationDock; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String transferOrderId;
        private TransferType transferType;
        private String sourceDock;
        private String destinationDock;

        public Builder transferOrderId(String transferOrderId) { this.transferOrderId = transferOrderId; return this; }
        public Builder transferType(TransferType transferType) { this.transferType = transferType; return this; }
        public Builder sourceDock(String sourceDock) { this.sourceDock = sourceDock; return this; }
        public Builder destinationDock(String destinationDock) { this.destinationDock = destinationDock; return this; }

        public TransferOrderCreatedEvent build() {
            return new TransferOrderCreatedEvent(transferOrderId, transferType, sourceDock, destinationDock);
        
}
}
}
