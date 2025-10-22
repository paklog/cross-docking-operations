package com.paklog.crossdocking.application.service;

import com.paklog.crossdocking.application.command.*;
import com.paklog.crossdocking.application.port.in.*;
import com.paklog.crossdocking.application.port.out.PublishEventPort;
import com.paklog.crossdocking.domain.aggregate.*;
import com.paklog.crossdocking.domain.event.DomainEvent;
import com.paklog.crossdocking.domain.repository.*;
import com.paklog.crossdocking.domain.service.*;
import com.paklog.crossdocking.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrossDockApplicationService implements CrossDockingUseCase {
    
    private final CrossDockOperationRepository operationRepository;
    private final TransferOrderRepository transferRepository;
    private final ConsolidationPlanRepository consolidationRepository;
    private final FlowOptimizationService optimizationService;
    private final ConsolidationService consolidationService;
    private final TransferCoordinationService coordinationService;
    private final PublishEventPort publishEventPort;
    
    @Override
    @Transactional
    public String createTransferOrder(CreateTransferOrderCommand command) {
        log.info("Creating transfer order: {} to {}", command.getSourceDockId(), command.getDestinationDockId());
        
        TransferOrder order = TransferOrder.builder()
            .id(UUID.randomUUID().toString())
            .transferNumber("XD-" + System.currentTimeMillis())
            .transferType(command.getTransferType())
            .status(FlowStatus.PLANNED)
            .sourceDockId(command.getSourceDockId())
            .destinationDockId(command.getDestinationDockId())
            .items(command.getItems())
            .timingWindow(command.getTimingWindow())
            .priority(command.getPriority())
            .inboundShipmentId(command.getInboundShipmentId())
            .outboundShipmentId(command.getOutboundShipmentId())
            .build();
        
        TransferOrder saved = transferRepository.save(order);
        publishDomainEvents(saved);
        
        return saved.getId();
    }
    
    @Override
    @Transactional
    public void initiateDirectTransfer(InitiateDirectTransferCommand command) {
        log.info("Initiating direct transfer: {}", command.getTransferOrderId());
        
        TransferOrder order = transferRepository.findById(command.getTransferOrderId())
            .orElseThrow(() -> new IllegalArgumentException("Transfer order not found"));
        
        coordinationService.coordinateTransfer(order);
        order.initiate();
        
        TransferOrder saved = transferRepository.save(order);
        publishDomainEvents(saved);
    }
    
    @Override
    @Transactional
    public String createConsolidation(CreateConsolidationCommand command) {
        log.info("Creating consolidation for {} orders", command.getTransferOrderIds().size());
        
        List<TransferOrder> orders = command.getTransferOrderIds().stream()
            .map(id -> transferRepository.findById(id).orElse(null))
            .filter(Objects::nonNull)
            .toList();
        
        ConsolidationPlan plan = consolidationService.createPlan(orders, command.getStrategy());
        plan.setConsolidatedDestination(command.getDestinationDock());
        
        ConsolidationPlan saved = consolidationRepository.save(plan);
        publishDomainEvents(saved);
        
        return saved.getId();
    }
    
    @Override
    @Transactional
    public void completeOperation(CompleteOperationCommand command) {
        log.info("Completing operation: {}", command.getOperationId());
        
        CrossDockOperation operation = operationRepository.findById(command.getOperationId())
            .orElseThrow(() -> new IllegalArgumentException("Operation not found"));
        
        operation.updateProgress(command.getItemsProcessed());
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
