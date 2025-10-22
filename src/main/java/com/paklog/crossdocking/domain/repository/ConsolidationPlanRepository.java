package com.paklog.crossdocking.domain.repository;

import com.paklog.crossdocking.domain.aggregate.ConsolidationPlan;
import java.util.*;

public interface ConsolidationPlanRepository {
    ConsolidationPlan save(ConsolidationPlan plan);
    Optional<ConsolidationPlan> findById(String id);
    List<ConsolidationPlan> findAll();
    void deleteById(String id);
}
