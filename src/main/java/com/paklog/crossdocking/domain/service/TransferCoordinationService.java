package com.paklog.crossdocking.domain.service;

import com.paklog.crossdocking.domain.aggregate.TransferOrder;
import com.paklog.crossdocking.domain.valueobject.TimingWindow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransferCoordinationService {
    
    public void coordinateTransfer(TransferOrder order) {
        log.info("Coordinating transfer: {}", order.getTransferNumber());
        
        TimingWindow window = order.getTimingWindow();
        if (window != null && window.isExpired()) {
            log.warn("Transfer window expired for order: {}", order.getId());
        }
    }
}
