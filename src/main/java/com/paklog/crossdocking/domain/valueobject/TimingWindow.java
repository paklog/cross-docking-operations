package com.paklog.crossdocking.domain.valueobject;

import lombok.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimingWindow {
    private Instant receiveBy;
    private Instant shipBy;
    private Integer maxDwellMinutes;
    private String priority;
    
    public boolean isExpired() {
        return Instant.now().isAfter(shipBy);
    }
    
    public long getRemainingMinutes() {
        long diff = shipBy.toEpochMilli() - Instant.now().toEpochMilli();
        return diff / 60000;
    }
}
