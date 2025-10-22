package com.paklog.crossdocking.domain.service;

import com.paklog.crossdocking.domain.aggregate.*;
import com.paklog.crossdocking.domain.valueobject.ConsolidationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class ConsolidationService {
    
    public ConsolidationPlan createPlan(List<TransferOrder> orders, ConsolidationStrategy strategy) {
        log.info("Creating consolidation plan for {} orders using strategy: {}", orders.size(), strategy);
        
        return ConsolidationPlan.builder()
            .id(UUID.randomUUID().toString())
            .planNumber("CONS-" + System.currentTimeMillis())
            .strategy(strategy)
            .status("PLANNED")
            .sourceTransferIds(new ArrayList<>())
            .itemsBySource(new HashMap<>())
            .build();
    }
}
