package com.paklog.crossdocking.application.command;

import jakarta.validation.constraints.NotBlank;

public record CompleteOperationCommand(
    @NotBlank
    String operationId,
    Integer itemsProcessed
) {}
