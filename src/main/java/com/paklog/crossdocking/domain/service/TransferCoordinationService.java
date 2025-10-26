package com.paklog.crossdocking.domain.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paklog.crossdocking.domain.aggregate.TransferOrder;
import com.paklog.crossdocking.domain.valueobject.TimingWindow;
import org.springframework.stereotype.Service;

@Service
public class TransferCoordinationService {
    private static final Logger log = LoggerFactory.getLogger(TransferCoordinationService.class);

    
    public void coordinateTransfer(TransferOrder order) {
        log.info("Coordinating transfer: {}", order.getTransferNumber());
        
        TimingWindow window = order.getTimingWindow();
        if (window != null && window.isExpired()) {
            log.warn("Transfer window expired for order: {}", order.getId());
        

}
}
}
