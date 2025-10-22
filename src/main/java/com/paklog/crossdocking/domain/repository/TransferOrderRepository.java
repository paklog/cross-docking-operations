package com.paklog.crossdocking.domain.repository;

import com.paklog.crossdocking.domain.aggregate.TransferOrder;
import com.paklog.crossdocking.domain.valueobject.FlowStatus;
import java.util.*;

public interface TransferOrderRepository {
    TransferOrder save(TransferOrder order);
    Optional<TransferOrder> findById(String id);
    List<TransferOrder> findAll();
    List<TransferOrder> findByStatus(FlowStatus status);
    void deleteById(String id);
}
