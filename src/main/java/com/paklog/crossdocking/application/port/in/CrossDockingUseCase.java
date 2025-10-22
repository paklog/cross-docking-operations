package com.paklog.crossdocking.application.port.in;

import com.paklog.crossdocking.application.command.*;
import com.paklog.crossdocking.domain.aggregate.CrossDockOperation;
import java.util.List;

public interface CrossDockingUseCase {
    String createTransferOrder(CreateTransferOrderCommand command);
    void initiateDirectTransfer(InitiateDirectTransferCommand command);
    String createConsolidation(CreateConsolidationCommand command);
    void completeOperation(CompleteOperationCommand command);
    CrossDockOperation getOperation(String operationId);
    List<CrossDockOperation> getActiveOperations();
}
