package com.paklog.crossdocking.infrastructure.persistence.document;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.Instant;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
