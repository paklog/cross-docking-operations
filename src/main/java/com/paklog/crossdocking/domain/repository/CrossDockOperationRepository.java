package com.paklog.crossdocking.domain.repository;

import com.paklog.crossdocking.domain.aggregate.CrossDockOperation;
import com.paklog.crossdocking.domain.valueobject.FlowStatus;
import java.util.*;

public interface CrossDockOperationRepository {
    CrossDockOperation save(CrossDockOperation operation);
    Optional<CrossDockOperation> findById(String id);
    List<CrossDockOperation> findAll();
    List<CrossDockOperation> findByStatus(FlowStatus status);
    List<CrossDockOperation> findActiveOperations();
    void deleteById(String id);
}
