package com.paklog.crossdocking.infrastructure.persistence.repository;

import com.paklog.crossdocking.infrastructure.persistence.document.TransferOrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MongoTransferOrderRepository extends MongoRepository<TransferOrderDocument, String> {
    List<TransferOrderDocument> findByStatus(String status);
    List<TransferOrderDocument> findBySourceDockId(String sourceDockId);
}
