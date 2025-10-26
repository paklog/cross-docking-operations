package com.paklog.crossdocking.application.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paklog.crossdocking.application.command.*;
import com.paklog.crossdocking.application.port.in.*;
import com.paklog.crossdocking.application.port.out.PublishEventPort;
import com.paklog.crossdocking.domain.aggregate.*;
import com.paklog.crossdocking.domain.event.DomainEvent;
import com.paklog.crossdocking.domain.repository.*;
import com.paklog.crossdocking.domain.service.*;
import com.paklog.crossdocking.domain.valueobject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class CrossDockApplicationService implements CrossDockingUseCase {
    private static final Logger log = LoggerFactory.getLogger(CrossDockApplicationService.class);

    
    private final CrossDockOperationRepository operationRepository;
    private final TransferOrderRepository transferRepository;
    private final ConsolidationPlanRepository consolidationRepository;
    private final FlowOptimizationService optimizationService;
    private final ConsolidationService consolidationService;
    private final TransferCoordinationService coordinationService;
    private final PublishEventPort publishEventPort;
    public CrossDockApplicationService(CrossDockOperationRepository operationRepository, TransferOrderRepository transferRepository, ConsolidationPlanRepository consolidationRepository, FlowOptimizationService optimizationService, ConsolidationService consolidationService, TransferCoordinationService coordinationService, PublishEventPort publishEventPort) {
        this.operationRepository = operationRepository;
        this.transferRepository = transferRepository;
        this.consolidationRepository = consolidationRepository;
        this.optimizationService = optimizationService;
        this.consolidationService = consolidationService;
        this.coordinationService = coordinationService;
        this.publishEventPort = publishEventPort;
    }

    
    @Override
    @Transactional
    public String createTransferOrder(CreateTransferOrderCommand command) {
        log.info("Creating transfer order: {} to {}", command.sourceDockId(), command.destinationDockId());
        
        TransferOrder order = TransferOrder.builder()
            .id(UUID.randomUUID().toString())
            .transferNumber("XD-" + System.currentTimeMillis())
            .transferType(command.transferType())
            .status(FlowStatus.PLANNED)
            .sourceDockId(command.sourceDockId())
            .destinationDockId(command.destinationDockId())
            .items(command.items())
            .timingWindow(command.timingWindow())
            .priority(command.priority())
            .inboundShipmentId(command.inboundShipmentId())
            .outboundShipmentId(command.outboundShipmentId())
            .build();
        
        TransferOrder saved = transferRepository.save(order);
        publishDomainEvents(saved);

        return saved.getId();
    }
    
    @Override
    @Transactional
    public void initiateDirectTransfer(InitiateDirectTransferCommand command) {
        log.info("Initiating direct transfer: {}", command.transferOrderId());
        
        TransferOrder order = transferRepository.findById(command.transferOrderId())
            .orElseThrow(() -> new IllegalArgumentException("Transfer order not found"));
        
        coordinationService.coordinateTransfer(order);
        order.initiate();
        
        TransferOrder saved = transferRepository.save(order);
        publishDomainEvents(saved);
    }
    
    @Override
    @Transactional
    public String createConsolidation(CreateConsolidationCommand command) {
        log.info("Creating consolidation for {} orders", command.transferOrderIds().size());
        
        List<TransferOrder> orders = command.transferOrderIds().stream()
            .map(id -> transferRepository.findById(id).orElse(null))
            .filter(Objects::nonNull)
            .toList();
        
        ConsolidationPlan plan = consolidationService.createPlan(orders, command.strategy());
        plan.setConsolidatedDestination(command.destinationDock());
        
        ConsolidationPlan saved = consolidationRepository.save(plan);
        publishDomainEvents(saved);

        return saved.getId();
    }
    
    @Override
    @Transactional
    public void completeOperation(CompleteOperationCommand command) {
        log.info("Completing operation: {}", command.operationId());
        
        CrossDockOperation operation = operationRepository.findById(command.operationId())
            .orElseThrow(() -> new IllegalArgumentException("Operation not found"));
        
        operation.updateProgress(command.itemsProcessed());
        operation.complete();
        
        CrossDockOperation saved = operationRepository.save(operation);
        publishDomainEvents(saved);
    }
    
    @Override
    public CrossDockOperation getOperation(String operationId) {
        return operationRepository.findById(operationId)
            .orElseThrow(() -> new IllegalArgumentException("Operation not found"));
    }
    
    @Override
    public List<CrossDockOperation> getActiveOperations() {
        return operationRepository.findActiveOperations();
    }
    
    private void publishDomainEvents(Object aggregate) {
        List<DomainEvent> events = null;
        
        if (aggregate instanceof TransferOrder) {
            events = ((TransferOrder) aggregate).getDomainEvents();
            ((TransferOrder) aggregate).clearDomainEvents();
        } else if (aggregate instanceof CrossDockOperation) {
            events = ((CrossDockOperation) aggregate).getDomainEvents();
            ((CrossDockOperation) aggregate).clearDomainEvents();
        } else if (aggregate instanceof ConsolidationPlan) {
            events = ((ConsolidationPlan) aggregate).getDomainEvents();
            ((ConsolidationPlan) aggregate).clearDomainEvents();
        }
        
        if (events != null) {
            events.forEach(event -> {
                try {
                    publishEventPort.publishEvent(event);
                } catch (Exception e) {
                    log.error("Failed to publish event: {}", event.getEventType(), e);
                }
            });
        }
    }
}
