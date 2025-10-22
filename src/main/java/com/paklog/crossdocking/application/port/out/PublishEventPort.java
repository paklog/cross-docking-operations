package com.paklog.crossdocking.application.port.out;

import com.paklog.crossdocking.domain.event.DomainEvent;

public interface PublishEventPort {
    void publishEvent(DomainEvent event);
}
