package com.paklog.crossdocking.application.command;

import com.paklog.crossdocking.domain.valueobject.*;
import jakarta.validation.constraints.*;
import java.util.*;

public record CreateTransferOrderCommand(
    @NotNull
    TransferType transferType,
    @NotBlank
    String sourceDockId,
    @NotBlank
    String destinationDockId,
    List<Map<String, Object>> items,
    TimingWindow timingWindow,
    TransferPriority priority,
    String inboundShipmentId,
    String outboundShipmentId
) {}
