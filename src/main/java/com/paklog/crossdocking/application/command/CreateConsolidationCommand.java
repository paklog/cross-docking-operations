package com.paklog.crossdocking.application.command;

import com.paklog.crossdocking.domain.valueobject.ConsolidationStrategy;
import jakarta.validation.constraints.*;
import java.util.List;

public record CreateConsolidationCommand(
    @NotEmpty
    List<String> transferOrderIds,
    @NotBlank
    String destinationDock,
    @NotNull
    ConsolidationStrategy strategy
) {}
