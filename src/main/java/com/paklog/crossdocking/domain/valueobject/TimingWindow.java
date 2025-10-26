package com.paklog.crossdocking.domain.valueobject;

import java.time.Instant;

public record TimingWindow(
    Instant receiveBy,
    Instant shipBy,
    Integer maxDwellMinutes,
    String priority
) {
    public boolean isExpired() {
        return shipBy != null && Instant.now().isAfter(shipBy);
    }
}
