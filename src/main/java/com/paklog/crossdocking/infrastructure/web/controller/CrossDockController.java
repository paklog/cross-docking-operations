package com.paklog.crossdocking.infrastructure.web.controller;

import com.paklog.crossdocking.application.command.*;
import com.paklog.crossdocking.application.port.in.CrossDockingUseCase;
import com.paklog.crossdocking.domain.aggregate.CrossDockOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/cross-dock")
@RequiredArgsConstructor
@Tag(name = "Cross-Docking", description = "Cross-docking operations")
public class CrossDockController {
    
    private final CrossDockingUseCase crossDockingUseCase;
    
    @PostMapping("/transfers")
    @Operation(summary = "Create transfer order")
    public ResponseEntity<String> createTransfer(@Valid @RequestBody CreateTransferOrderCommand command) {
        log.info("REST: Creating transfer order");
        String orderId = crossDockingUseCase.createTransferOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }
    
    @PostMapping("/transfers/{id}/initiate")
    @Operation(summary = "Initiate direct transfer")
    public ResponseEntity<Void> initiateTransfer(@PathVariable String id) {
        log.info("REST: Initiating transfer: {}", id);
        InitiateDirectTransferCommand command = InitiateDirectTransferCommand.builder()
            .transferOrderId(id)
            .build();
        crossDockingUseCase.initiateDirectTransfer(command);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/consolidations")
    @Operation(summary = "Create consolidation plan")
    public ResponseEntity<String> createConsolidation(@Valid @RequestBody CreateConsolidationCommand command) {
        log.info("REST: Creating consolidation");
        String planId = crossDockingUseCase.createConsolidation(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(planId);
    }
    
    @PostMapping("/operations/{id}/complete")
    @Operation(summary = "Complete cross-dock operation")
    public ResponseEntity<Void> completeOperation(@PathVariable String id, @RequestParam Integer itemsProcessed) {
        log.info("REST: Completing operation: {}", id);
        CompleteOperationCommand command = CompleteOperationCommand.builder()
            .operationId(id)
            .itemsProcessed(itemsProcessed)
            .build();
        crossDockingUseCase.completeOperation(command);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/operations/{id}")
    @Operation(summary = "Get operation by ID")
    public ResponseEntity<CrossDockOperation> getOperation(@PathVariable String id) {
        CrossDockOperation operation = crossDockingUseCase.getOperation(id);
        return ResponseEntity.ok(operation);
    }
    
    @GetMapping("/operations/active")
    @Operation(summary = "Get all active operations")
    public ResponseEntity<List<CrossDockOperation>> getActiveOperations() {
        List<CrossDockOperation> operations = crossDockingUseCase.getActiveOperations();
        return ResponseEntity.ok(operations);
    }
}
