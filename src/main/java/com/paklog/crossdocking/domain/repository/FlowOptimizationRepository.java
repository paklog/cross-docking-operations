package com.paklog.crossdocking.domain.repository;

import com.paklog.crossdocking.domain.aggregate.FlowOptimization;
import java.util.*;

public interface FlowOptimizationRepository {
    FlowOptimization save(FlowOptimization optimization);
    Optional<FlowOptimization> findById(String id);
    List<FlowOptimization> findAll();
    void deleteById(String id);
}
